<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :xs="24" :sm="12" :md="4" :lg="4" :xl="4">
        <el-input v-model="searchKey" placeholder="请输入角色中文名称" size="small" clearable></el-input>
      </el-col>
      <el-col :xs="24" :sm="12" :md="20" :lg="20" :xl="20">
        <el-button @click="clickSearch" icon="el-icon-search" size="small" circle></el-button>
        <el-button @click="clickSearch" size="small" icon="el-icon-refresh" circle></el-button>
        <el-button @click="clickAdd" size="small" type="primary" icon="el-icon-s-custom">添加角色</el-button>
      </el-col>
    </el-row>
    <br/>
    <el-table v-loading="listLoading" :data="list" element-loading-text="加载中..." highlight-current-row stripe
              @sort-change="sortChange">
      <el-table-column type="index" label="序号" width="80" align="center">
        <template slot-scope="scope"><span>{{ scope.$index + (pageNum - 1) * pageSize + 1 }} </span></template>
      </el-table-column>
      <el-table-column label="角色英文名" align="center" sortable="custom" prop="role_name_en">
        <template slot-scope="scope">{{ scope.row.roleNameEn }}</template>
      </el-table-column>
      <el-table-column
          label="角色中文名" align="center" sortable="custom" prop="role_name_cn">
        <template slot-scope="scope">{{ scope.row.roleNameCn }}</template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" sortable="custom" prop="create_time">
        <template slot-scope="scope">
          <i class="el-icon-time"/>
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" sortable="custom" prop="state">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.state == 1" type="success" style="line-height: 35px;">
            <i class="el-icon-key" style="font-size:16px;"></i>
          </el-tag>
          <el-tag v-if="scope.row.state == 0" type="danger" style="line-height: 35px;">
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
    <el-pagination background layout="prev, pager, next" :total="total" :current-page="pageNum" :page-size="pageSize"
                   :page-count="pages" :pager-count="5" @next-click="nextClick" @current-change="currentChange"
                   @prev-click="prevClick"></el-pagination>
    <el-dialog :title="form.title" :visible.sync="form.isShow" center width="45%" :close-on-click-modal="false">
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="角色英文名称">
          <el-input v-model="form.roleNameEn" size="small"></el-input>
        </el-form-item>
        <el-form-item label="角色中文名称">
          <el-input v-model="form.roleNameCn" size="small"></el-input>
        </el-form-item>
        <el-form-item label="拥有模块">
          <el-tree ref="routerTree" v-model="form.router.defaultChecked" :data="form.router.list"
                   :props="form.defaultProps" show-checkbox @check="check2" :default-checked-keys="form.router.defaultChecked" node-key="id" accordion></el-tree>
        </el-form-item>
        <el-form-item label="拥有权限">
          <el-tree ref="authorityTree" v-model="form.treeChecked" :data="form.authorityList" :props="form.defaultProps"
                   show-checkbox @check="check1" :default-checked-keys="form.treeChecked" node-key="id" accordion
          ></el-tree>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.state" active-color="#13ce66" inactive-color="#ff4949" active-text="正常"
                     inactive-text="锁定"></el-switch>
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


import { authorityTreeApi, delRoleApi, listRoleApi, roleInfoApi, routerTreeApi, saveApi } from '../../api/role'

export default {
  data() {
    return {
      searchKey: '', //搜索关键字
      columnName: '', //排序的列名
      order: '', //排序方式
      list: null, //数据集合
      listLoading: true, //加载
      pageNum: 1, //当前页
      pageSize: 10, //每页显示多少条数据
      total: 0, //总条数
      prePage: 0, //上一页
      nextPage: 0, //下一页
      pages: 0, //总页数
      //表单信息
      form: {
        title: '', //表单标题
        //保存类型  1: 新增  2:修改  3:删除
        type: null,
        //角色ID
        roleId: null,
        // 角色英文名称
        roleNameEn: 'ROLE_',
        // 角色中文名称
        roleNameCn: '',
        //角色状态
        state: true,
        //角色父级ID,
        roleParentId: null,
        //表单是否显示
        isShow: false,
        //按钮是否显示
        buttonShow: false,
        // 拥有权限树形数据
        authorityList: [],
        //拥有权限默认选中
        treeChecked: [],
        //选中的节点
        authorityCheckedList: [],
        //默认绑定的节点属性
        defaultProps: {
          children: 'children',
          label: 'label',
          disabled: this.isDisabled
        },
        router: {
          // 路由数据
          list: [],
          // 默认选中
          defaultChecked: []
        },
        //是否操作过复选框
        checkFlag1: false,
        checkFlag2: false
      }
    }
  },
  created() {
    this.listRole()
    this.authorityTree()
    this.routerTree()
  },
  methods: {
    isDisabled(data, node) {
      //点击查看的时候禁用全部树节点，其他的就是哪个节点被禁用就禁用哪个
      if (this.form.title == '查看角色信息') {
        return true
      } else {
        return data.disabled
      }
    },
    listRole() {
      this.listLoading = true
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        columnName: this.columnName,
        order: this.order,
        roleName: this.searchKey
      }
      listRoleApi(params).then(res => {
        if (res.data.resultCode == 1) {
          this.list = res.data.resultData.list
          this.total = parseInt(res.data.resultData.total)
          this.pageNum = parseInt(res.data.resultData.pageNum)
          this.pageSize = parseInt(res.data.resultData.pageSize)
          this.prePage = parseInt(res.data.resultData.prePage)
          this.nextPage = parseInt(res.data.resultData.nextPage)
          this.pages = parseInt(res.data.resultData.pages)
          this.listLoading = false
        } else {
          this.$message({
            message: res.data.resultMessage,
            type: 'error'
          })
          this.listLoading = false
        }
      })
    },
    nextClick() {
      this.pageNum = this.nextPage
      this.listRole()
    },
    prevClick() {
      this.pageNum = this.prePage
      this.listRole()
    },
    currentChange(pageNum) {
      this.pageNum = pageNum
      this.listRole()
    },
    clickAdd() {
      this.form.isShow = true
      this.form.buttonShow = true
      this.form.title = '添加角色'
      this.form.type = 1
      this.form.roleId = null
      this.clear()
    },
    roleInfo() {
      roleInfoApi(this.form.roleId).then(res => {
        if (res.data.resultCode == 1) {
          this.form.treeChecked = res.data.resultData.authorityDefaultChecked
          this.form.router.defaultChecked = res.data.resultData.routerDefaultChecked
          this.form.roleNameCn = res.data.resultData.roleNameCn
          this.form.roleNameEn = res.data.resultData.roleNameEn
          this.form.state = res.data.resultData.state
        } else {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    clickView(index, row) {
      this.clear()
      this.form.roleId = row.id
      this.form.isShow = true
      this.form.buttonShow = false
      this.form.title = '查看角色信息'
      this.form.type = 2
      this.roleInfo()
    },
    clickEdit(index, row) {
      this.clear()
      this.form.roleId = row.id
      this.form.isShow = true
      this.form.buttonShow = true
      this.form.title = '编辑角色信息'
      this.form.type = 2
      this.roleInfo()
    },
    clickDel(index, row) {
      this.form.isShow = false
      this.form.roleId = row.id
      this.form.authorityCheckedList = []
      this.form.type = 3
      this.$confirm('确定删除角色 `' + row.roleNameCn + '` 吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delRoleApi(this.form.roleId).then(res => {
          if (res.data.resultCode == 1) {
            this.$message.success(res.data.resultMessage)
            this.list.splice(index, 1)
          } else {
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
    sortChange(data) {
      this.columnName = data.prop
      this.order = data.order
      this.listRole()
    },
    clickSearch() {
      this.listRole()
    },
    clear() {
      //显示表单
      this.form.isShow = true
      //异步渲染DOM钩子函数
      this.$nextTick(function() {
        //收缩所有节点
        for (var i = 0; i < this.$refs.authorityTree.store._getAllNodes().length; i++) {
          this.$refs.authorityTree.store._getAllNodes()[i].expanded = false
        }
        //清空所有选中的节点
        this.$refs.authorityTree.setCheckedKeys([])

        //收缩所有节点
        for (var i = 0; i < this.$refs.routerTree.store._getAllNodes().length; i++) {
          this.$refs.routerTree.store._getAllNodes()[i].expanded = false
        }
        //清空所有选中的节点
        this.$refs.routerTree.setCheckedKeys([])
      })

      this.form.state = true
      //清空默认选中的权限树节点
      this.form.treeChecked = []
      this.form.router.defaultChecked = []
      this.form.authorityCheckedList = []

      //显示所属权限
      this.form.authorityShow = true
      //显示按钮
      this.form.buttonShow = true
      //清空角色名称输入框
      this.form.roleNameEn = 'ROLE_'
      this.form.roleNameCn = ''
      //默认为没有操作过复选框
      this.form.checkFlag1 = false
      this.form.checkFlag2 = false
    },
    check1() {
      this.form.checkFlag1 = true
    },
    check2() {
      this.form.checkFlag2 = true
    },
    /**
     * 渲染权限树
     */
    authorityTree() {
      authorityTreeApi().then(res => {
        if (res.data.resultCode == 1) {
          this.form.authorityList = res.data.resultData
        } else {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 渲染路由树
     */
    routerTree() {
      routerTreeApi().then(res => {
        if (res.data.resultCode == 1) {
          this.form.router.list = res.data.resultData
        } else {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 提交数据
     */
    onSubmit() {
      const params = {
        type: this.form.type,
        role: {
          id: this.form.roleId,
          roleNameCn: this.form.roleNameCn,
          roleNameEn: this.form.roleNameEn,
          state: this.form.state
        },
        checkFlag1: this.form.checkFlag1,
        checkFlag2: this.form.checkFlag2,
        authorityIds: this.$refs.authorityTree.getCheckedKeys(true),
        routerIds: this.$refs.routerTree.getCheckedKeys(false)
      }
      if (params.role.roleNameEn == '') {
        this.$message.error('请输入角色英文名称')
        return
      }
      if (params.role.roleNameCn == '') {
        this.$message.error('请输入角色中文名称')
        return
      }
      if (params.authorityIds.length == 0) {
        this.$message.error('请为该角色添加权限')
        return
      }
      if (params.routerIds.length == 0) {
        this.$message.error('请为该角色添加路由模块')
        return
      }

      console.log(params)

      saveApi(this.form.type, params).then(res => {
        if (res.data.resultCode == 1) {
          this.searchKey = ''
          this.$message.success(res.data.resultMessage)
          if (params.type != 2) {
            this.clear()
          }
          this.listRole()
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    }
  }
}
</script>
<style lang="css" scoped>
</style>
