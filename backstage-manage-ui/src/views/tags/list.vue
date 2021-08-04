<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :xs="24" :sm="24" :md="4" :lg="4" :xl="4">
        <el-input v-model="searchKey" placeholder="请输入标签名称" size="small" clearable></el-input>
      </el-col>
      <el-col :xs="24" :sm="24" :md="20" :lg="20" :xl="20">
        <el-button @click="clickSearch" icon="el-icon-search" size="small" circle></el-button>
        <el-button @click="clickSearch" size="small" icon="el-icon-refresh" circle></el-button>
      </el-col>
    </el-row>
    <br/>
    <el-table v-loading="listLoading" :data="list" element-loading-text="加载中..." highlight-current-row @sort-change="sortChange">
      <el-table-column type="index" label="序号" width="80" align="center">
        <template slot-scope="scope"><span>{{ scope.$index + (pageNum - 1) * pageSize + 1 }} </span></template>
      </el-table-column>
      <el-table-column label="标签名称" align="center" sortable="custom" prop="tag_name">
        <template slot-scope="scope">
          <span>{{ scope.row.tagName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="标签LOGO" align="center">
        <template slot-scope="scope">
          <img :src="scope.row.tagLogo" width="28" height="28"/>
        </template>
      </el-table-column>
      <el-table-column label="标签类型" align="center" sortable="custom" prop="tag_type">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.tagType==1" type="success">单个</el-tag>
          <el-tag v-if="scope.row.tagType==2" type="warning">组合</el-tag>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="clickEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="clickDel(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <br/>
    <el-pagination background layout="prev, pager, next" :total="total" :current-page="pageNum" :page-size="pageSize" :page-count="pages" :pager-count="5" @next-click="nextClick"
                   @current-change="currentChange" @prev-click="prevClick"></el-pagination>
    <el-dialog :title="dialog.dialogTitle" :visible.sync="dialog.dialogVisible" width="500px" center :close-on-click-modal="false">
      <el-form :model="dialog">
        <el-form-item label="标签名" :label-width="dialog.formLabelWidth">
          <el-input size="small" v-model="dialog.tagName" autocomplete="off" :disabled="dialog.isView"></el-input>
        </el-form-item>
        <el-form-item label="标签Logo" :label-width="dialog.formLabelWidth">
          <el-input size="small" v-model="dialog.tagLogo" autocomplete="off" :disabled="dialog.isView"></el-input>
          <img :src="dialog.tagLogo" width="80" height="80" style="margin-top: 20px;border-radius: 50%;float: left;"/>
          <el-upload :action="uploadImgAction" class="avatar-uploader" :show-file-list="false" :before-upload="beforeAvatarUpload" :http-request="uploadImage">
            <i class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="标签类型" :label-width="dialog.formLabelWidth">
          <el-radio v-model="dialog.tagType" label="1">个体</el-radio>
          <el-radio v-model="dialog.tagType" label="2">组合</el-radio>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.dialogVisible = false" size="small">返回</el-button>
        <el-button type="primary" v-if="dialog.isView==false" size="small" @click="clickSave">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { listTagApi, saveApi, tagInfoApi, uploadLogoApi, delTagApi } from '../../api/tag'

export default {
  data() {
    return {
      searchKey: '', // 搜索关键字
      columnName: '', // 排序的列名
      order: '', // 排序的方式
      uploadImgAction: '',
      list: null, //数据集合
      listLoading: true, //加载
      pageNum: 1, //当前页
      pageSize: 15, //每页显示多少条数据
      total: 0, //总条数
      prePage: 0, //上一页
      nextPage: 0, //下一页
      pages: 0, //总页数
      dialog: {
        dialogVisible: false, //查看窗口是否显示
        dialogTitle: '编辑', //窗口标题
        id: 0,
        tagName: '', //网站标题
        tagType: '', //标签类型
        tagLogo: '', //网站logo
        formLabelWidth: '80px', //显示宽度
        isView: false //是否为查看 false 为编辑，true为查看
      }
    }
  },
  created() {
    this.listTag()
  },
  methods: {
    /**
     * 单击查询
     */
    clickSearch() {
      this.listTag()
    },
    /**
     * 当前排序改变是触发
     * @param data 需要设置prop属性来获取排序的列名
     */
    sortChange(data) {
      this.columnName = data.prop
      this.order = data.order
      this.listTag()
    },
    /**
     * 获取数据
     * @param pageNum 当前页
     * @param pageSize 每页的显示的数量
     */
    listTag() {
      this.listLoading = true
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        tagName: this.searchKey,
        columnName: this.columnName,
        order: this.order
      }
      listTagApi(params).then(res => {
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
    nextClick() {
      this.pageNum = this.nextPage
      this.listTag()
    },
    prevClick() {
      this.pageNum = this.prePage
      this.listTag()
    },
    currentChange(pageNum) {
      this.pageNum = pageNum
      this.listTag()
    },
    /**
     * 上传图片之前的回调函数事件
     * @param file 文件
     * @returns {boolean}
     */
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/svg+xml'
      const isLt2M = file.size / 1024 / 1024 < 5

      if (!isJPG) {
        this.$message.error('上传标签logo只能是 svg 格式')
      }
      if (!isLt2M) {
        this.$message.error('上传标签Logo大小不能超过 5MB')
      }
      return isJPG && isLt2M
    },
    /**
     * 自定义覆盖上传函数
     * @param $file 文件信息
     */
    uploadImage($file) {
      const formData = new FormData()
      formData.append('image', $file.file)
      formData.append('uploadPrefix', 'tag')
      uploadLogoApi(formData).then(res => {
        if (res.data.resultCode == 1) {
          this.dialog.tagLogo = res.data.resultData
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 单击编辑
     * @param index
     * @param row
     */
    clickEdit(index, row) {
      const id = row.tagId
      this.dialog.id = id
      tagInfoApi(id).then(res => {
        if (res.data.resultCode == 1) {
          this.dialog.tagName = res.data.resultData.tagName
          this.dialog.tagType = res.data.resultData.tagType + ''
          this.dialog.tagLogo = res.data.resultData.tagLogo
          this.dialog.dialogVisible = true
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 单击保存
     */
    clickSave() {
      const params = {
        tagId: this.dialog.id,
        tagName: this.dialog.tagName,
        tagLogo: this.dialog.tagLogo,
        tagType: this.dialog.tagType
      }
      saveApi('edit', params).then(res => {
        if (res.data.resultCode == 1) {
          this.$message.success(res.data.resultMessage)
          this.dialog.dialogVisible = false
          this.listTag()
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    clickDel(index, row) {
      this.$confirm('确定删除该标签吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delTagApi(row.tagId).then(res => {
          if (res.data.resultCode == 1) {
            this.$message.success(res.data.resultMessage)
            this.list.splice(index, 1)
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
    }
  }
}
</script>
<style lang="css" scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  margin-top: 20px;
  margin-left: 20px;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 80px;
  height: 80px;
  line-height: 80px;
  text-align: center;
  margin-top: 20px;
  margin-left: 20px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
}
</style>
