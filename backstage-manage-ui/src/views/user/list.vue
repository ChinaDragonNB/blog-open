<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :xs="24" :sm="12" :md="4" :lg="4" :xl="4">
        <el-input v-model="searchKey" placeholder="请输入用户昵称" size="small" clearable></el-input>
      </el-col>
      <el-col :xs="24" :sm="12" :md="20" :lg="20" :xl="20">
        <el-button @click="clickSearch" icon="el-icon-search" size="small" circle></el-button>
        <el-button @click="clickSearch" size="small" icon="el-icon-refresh" circle></el-button>
        <el-button @click="clickAdd" size="small" type="primary" icon="el-icon-user-solid">添加用户</el-button>
      </el-col>
    </el-row>
    <br/>
    <el-table v-loading="listLoading" :data="list" element-loading-text="加载中..." highlight-current-row stripe @sort-change="sortChange">
      <el-table-column type="index" label="序号" width="80" align="center">
        <template slot-scope="scope"><span>{{ scope.$index + (pageNum - 1) * pageSize + 1 }} </span></template>
      </el-table-column>
      <el-table-column label="昵称" align="center" sortable="custom" prop="nick_name">
        <template slot-scope="scope">
          <span style>{{ scope.row.nickName }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="头像">
        <template slot-scope="scope">
          <img :src="scope.row.loginImg" style="width:50px;height:50px;border-radius:50%"/>
        </template>
      </el-table-column>
      <el-table-column label="角色" align="center" sortable="custom" prop="role_id">
        <template slot-scope="scope">
          <el-tag effect="plain">{{ scope.row.roleName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" sortable="custom" prop="state">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.state == 1" type="success" effect="dark" style="line-height: 35px;">
            <i class="el-icon-key" style="font-size:16px;"></i>
          </el-tag>
          <el-tag v-if="scope.row.state == 0" type="danger" effect="dark" style="line-height: 35px;">
            <i class="el-icon-lock" style="font-size:16px;"></i>
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="small" @click="clickView(scope.$index, scope.row)">查看</el-button>
          <el-button type="primary" size="small" @click="clickEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="clickDel(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <br/>
    <el-pagination background layout="prev, pager, next" :total="total" :current-page="pageNum" :page-size="pageSize" :page-count="pages" :pager-count="5" @next-click="nextClick"
                   @current-change="currentChange" @prev-click="prevClick"></el-pagination>
    <el-dialog :title="form.title" :visible.sync="form.isShow" center :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="用户昵称">
          <el-input v-model="form.nickName" size="small" :disabled="form.isDisabled"></el-input>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="form.userName" size="small" :disabled="form.isDisabled"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="form.userPass" size="small" show-password :disabled="form.isDisabled"></el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-input v-model="form.loginImg" size="small" :disabled="form.isDisabled"></el-input>
          <el-upload class="avatar-uploader" :action="form.uploadImgAction" :http-request="uploadImage" :show-file-list="false" :before-upload="beforeAvatarUpload"
                     :disabled="form.isDisabled">
            <img v-if="form.loginImg" :src="form.loginImg" class="avatar"/>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="博客主页">
          <el-input v-model="form.blogHome" size="small" :disabled="form.isDisabled"></el-input>
        </el-form-item>
        <el-form-item label="所属角色">
          <el-select v-model="form.roleId" placeholder="请选择" size="small" :disabled="form.isDisabled">
            <el-option v-for="item in form.roleList" :key="item.id" :label="item.roleNameCn" :value="item.id" :disabled="!item.state"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch :disabled="form.isDisabled" v-model="form.state" active-color="#13ce66" inactive-color="#ff4949" active-text="正常" inactive-text="锁定"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="onSubmit" size="small" v-show="form.buttonShow">保存</el-button>
        <el-button size="small" @click="form.isShow = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { delUserApi, listUserApi, roleListApi, saveApi, uploadImageApi, userInfoApi } from '@/api/user'

export default {
  data() {
    return {
      searchKey: '', //搜索内容
      list: null, //数据集合
      listLoading: true, //加载
      pageNum: 1, //当前页
      pageSize: 10, //每页显示多少条数据
      total: 0, //总条数
      prePage: 0, //上一页
      nextPage: 0, //下一页
      pages: 0, //总页数
      columnName: '', //列名
      order: '', //排序方式
      //表单信息
      form: {
        type: '', //提交类型
        title: '', //窗口标题
        buttonShow: true, //保存按钮是否显示
        isShow: false, //窗口是否显示
        id: '', //用户id
        nickName: '', //用户昵称
        userName: '', //用户名
        userPass: '', //用户密码
        loginImg: '', //用户头像
        roleList: [], //角色数据
        roleId: '', //所属角色
        state: true, //状态
        blogHome: '', //博客主页
        uploadImgAction: '', //头像上传地址
        isDisabled: false //控件是否禁用
      }
    }
  },
  created() {
    this.listUser()
    this.roleList()
  },
  methods: {
    clearAll() {
      this.form.nickName = ''
      this.form.userName = ''
      this.form.userPass = ''
      this.form.loginImg = ''
      this.form.blogHome = ''
      this.form.roleId = ''
      this.form.state = true
    },
    listUser() {
      this.listLoading = true
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        nickName: this.searchKey,
        columnName: this.columnName,
        order: this.order
      }
      listUserApi(params).then(res => {
        if (res.data.resultCode == 1) {
          this.list = res.data.resultData.list
          this.total = parseInt(res.data.resultData.total)
          this.pageNum = parseInt(res.data.resultData.pageNum)
          this.pageSize = parseInt(res.data.resultData.pageSize)
          this.prePage = parseInt(res.data.resultData.prePage)
          this.nextPage = parseInt(res.data.resultData.nextPage)
          this.pages = parseInt(res.data.resultData.pages)
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
        this.listLoading = false
      })
    },
    roleList() {
      roleListApi().then(res => {
        if (res.data.resultCode == 1) {
          this.form.roleList = res.data.resultData
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    nextClick() {
      this.pageNum = this.nextPage
      this.listUser()
    },
    prevClick() {
      this.pageNum = this.prePage
      this.listUser()
    },
    currentChange(pageNum) {
      this.pageNum = pageNum
      this.listUser()
    },
    clickAdd() {
      this.clearAll()
      this.form.isShow = true
      this.form.type = 1
      this.form.isDisabled = false
      this.form.buttonShow = true
    },
    userInfo(id) {
      this.form.id = id
      userInfoApi(id).then(res => {
        if (res.data.resultCode == 1) {
          this.form.nickName = res.data.resultData.nickName
          this.form.userName = res.data.resultData.userName
          this.form.userPass = res.data.resultData.userPass
          this.form.loginImg = res.data.resultData.loginImg
          this.form.blogHome = res.data.resultData.blogHome
          this.form.roleId = res.data.resultData.roleId
          this.form.state = res.data.resultData.state
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    clickView(index, row) {
      this.form.isShow = true
      this.form.isDisabled = true
      this.form.buttonShow = false
      this.userInfo(row.id)
    },
    clickEdit(index, row) {
      this.form.isShow = true
      this.form.type = 2
      this.form.isDisabled = false
      this.form.buttonShow = true
      this.userInfo(row.id)
    },
    clickDel(index, row) {
      this.$confirm('确定删除该用户吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delUserApi(row.id).then(res => {
          if (res.data.resultCode == 1) {
            this.searchKey = ''
            this.$message.success(res.data.resultMessage)
            switch (this.form.type) {
              case 1:
                this.clearAll()
                this.listUser()
                break
              case 2:
                this.listUser()
                break
              case 3:
                this.listUser()
                break
            }
          } else if (res.data.resultCode == -1) {
            this.$message.error(res.data.resultMessage)
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    /**
     * 单击查询
     */
    clickSearch() {
      this.listUser()
    },
    /**
     * 排序方式
     */
    sortChange(data) {
      this.columnName = data.prop
      this.order = data.order
      this.listUser()
    },
    /**
     * 上传头像
     */
    uploadImage($file) {
      const formData = new FormData()
      formData.append('image', $file.file)
      formData.append('uploadPrefix', 'customFile')
      uploadImageApi(formData).then(res => {
        if (res.data.resultCode == 1) {
          this.form.loginImg = res.data.resultData
        } else {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 上传头像之前
     */
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传的封面图片只能是 JPG 格式')
      }
      if (!isLt2M) {
        this.$message.error('上传封面图片大小不能超过 2MB')
      }
      return isJPG && isLt2M
    },
    /**
     * 提交数据
     */
    onSubmit() {
      if (this.form.nickName == '') {
        this.$message.error('请输入用户昵称')
        return
      }

      if (this.form.userName == '') {
        this.$message.error('请输入用户名')
        return
      }

      if (this.form.userPass == '') {
        this.$message.error('请输入密码')
        return
      }

      if (this.form.loginImg == '') {
        this.$message.error('请上传头像')
        return
      }

      if (this.form.blogHome == '') {
        this.$message.error('请输入博客主页')
        return
      }

      if (this.form.roleId == '') {
        this.$message.error('请选择所属角色')
        return
      }
      const params = {
        id: this.form.id, //用户ID
        nickName: this.form.nickName, //用户昵称
        userName: this.form.userName, //用户名
        userPass: this.form.userPass, //用户密码
        loginImg: this.form.loginImg, //用户头像
        roleId: this.form.roleId, //所属角色
        state: this.form.state, //状态
        blogHome: this.form.blogHome //博客主页

      }

      saveApi(this.form.type, params).then(res => {
        if (res.data.resultCode == 1) {
          this.$message.success(res.data.resultMessage)
          if (res.data.clear == true) {
            this.clearAll()
          }
          if (res.data.refresh == true) {
            this.listUser()
          }
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })

    }
  }
}
</script>
<style lang="css" scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #8c939d;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 50px;
  height: 50px;
  line-height: 50px;
  text-align: center;
  border: 1px dashed #8c939d;
  border-radius: 6px;
}

.avatar {
  width: 50px;
  height: 50px;
  display: block;
  border-radius: 10px;
}
</style>
