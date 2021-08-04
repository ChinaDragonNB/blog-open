<template>
  <div class="container">
    <el-row :gutter="30">
      <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
        <el-button @click="tree" size="small" icon="el-icon-refresh">刷新</el-button>
        <el-button size="small" @click="appendTop" type="primary" icon="el-icon-files">添加父级路由</el-button>
        <el-button size="small" @click="clickRouterOrder" type="primary" icon="el-icon-sort">路由排序</el-button>
      </el-col>
    </el-row>
    <br>
    <el-row :gutter="30">
      <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6">
        <el-tree ref="tree" :data="data" node-key="id" :expand-on-click-node="false" :indent="20" highlight-current @node-click="handleNodeClick" accordion>
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>{{ node.label }}</span>
            <span>
              <el-button type="text" size="small" @click.stop="append(data)">
                <i class="el-icon-circle-plus"></i>
              </el-button>
              <el-button type="text" size="small" @click.stop="remove(node, data)">
                <i class="el-icon-delete-solid"></i>
              </el-button>
            </span>
          </span>
        </el-tree>
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
        <el-form ref="form" :model="form.router" label-width="100px" v-show="form.isShow">
          <el-form-item label="组件名称">
            <el-input v-model="form.router.name" placeholder="组件名称" size="small"></el-input>
          </el-form-item>
          <el-form-item label="菜单标题">
            <el-input v-model="form.router.title" placeholder="标题" size="small"></el-input>
          </el-form-item>
          <el-form-item label="菜单路径">
            <el-input v-model="form.router.path" placeholder="菜单路径,显示在地址栏的路径" size="small"></el-input>
          </el-form-item>
          <el-form-item label="组件路径">
            <el-input v-model="form.router.component" placeholder="组件路径,views目录下的路径" size="small"></el-input>
          </el-form-item>
          <el-form-item label="重定向路径">
            <el-input v-model="form.router.redirect" placeholder="重定向菜单路径" size="small"></el-input>
          </el-form-item>
          <el-form-item label="图标">
            <el-input v-model="form.router.icon" placeholder="图标" size="small"></el-input>
          </el-form-item>
          <el-form-item label="路由描述">
            <el-input type="textarea" :rows="3" v-model="form.router.describe" placeholder="请输入路由描述" size="small"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit" size="small">保存</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <el-dialog title="排序" :visible.sync="orderDialogVisible" :close-on-click-modal="false">
      <el-table :data="orderTableData">
        <el-table-column type="index" label="序号" width="100"></el-table-column>
        <el-table-column prop="title" label="路由标题" width="180"></el-table-column>
        <el-table-column prop="path" label="路由路径"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button icon="el-icon-top" circle size="small" @click="clickTopMove(scope.$index, scope.row)"></el-button>
            <el-button icon="el-icon-bottom" circle size="small" @click="clickBottomMove(scope.$index, scope.row)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>

import { menuListApi, routerInfoApi, saveApi, treeApi, updateSortApi } from '../../api/router'

export default {
  data() {
    return {
      data: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      treeData: [],// 暂时保存树形数据
      form: {
        isShow: false, //表单是否显示
        type: 0,// 提交类型
        router: {
          id: '',  // 主键
          name: '', // 组件Name
          title: '',  // 组件标题
          path: '', // 路由路径
          component: '', // 组件路径
          redirect: '', // 重定向路径
          icon: '',  // 图标名
          hidden: '', // 是否隐藏
          parentId: '',  // 父级路由,如果没有,则为0
          noCache: '',  // 该路由是否不缓存
          describe: '',  // 该路由的描述
          alwaysShow: '',  // 如果设置为true，将始终显示根菜单
          activeMenu: '',       // 如果设置路径，侧栏将突出显示您设置的路径,一般只有子级菜单隐藏的时候才会设置
          orderIndex: ''  // 路由显示顺序
        }
      },
      orderDialogVisible: false, //排序窗口是否打开
      orderTableData: [] //排序窗口表格数据
    }
  },
  created() {
    this.tree()
  },
  methods: {
    tree() {
      treeApi().then(res => {
        if (res.data.resultCode == 1) {
          this.data = res.data.resultData
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    routerInfo() {
      this.form.type = 2
      routerInfoApi(this.form.router.id).then(res => {
        if (res.data.resultCode == 1) {
          this.form.router = res.data.resultData
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 单击任意节点时
     */
    handleNodeClick(data) {
      this.treeData = data
      this.form.isShow = true
      this.form.router.id = data.id
      //编辑的时候不需要父级节点
      this.form.router.parentId = null
      this.routerInfo()
    },
    /**
     * 单击添加顶级节点按钮时
     */
    appendTop() {
      this.treeData = []
      this.form.isShow = true
      this.form.type = 1
      this.form.router = {}
      //顶级节点父级ID默认为0
      this.form.router.parentId = 0
    },
    /**
     * 单击添加按钮时
     */
    append(data) {
      this.treeData = data
      this.form.isShow = true
      this.form.type = 1
      this.form.router = {}
      //当前节点ID为子节点的父ID
      this.form.router.parentId = data.id
    },
    /**
     * 单击移除按钮时
     */
    remove(node, data) {
      this.treeData = data
      this.form.router.id = data.id
      this.form.type = 3
      if (data.children != null) {
        if (data.children.length != 0) {
          this.$message.error('不能删除该节点,该节点下还有子节点!')
          return
        }
      }
      this.$confirm('确定删除该路由吗?', '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        this.save(this.form.router.id)
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    /**
     * 提交数据
     */
    onSubmit() {
      if (!this.form.router.title) {
        this.$message.error('请输入路由标题')
        return
      }
      if (!this.form.router.path) {
        this.$message.error('请输入路由路径')
        return
      }
      this.save(this.form.router)
    },
    /**
     * 执行请求
     */
    save(params) {
      saveApi(this.form.type, params).then(res => {
        if (res.data.resultCode == 1) {
          this.$message.success(res.data.resultMessage)
          switch (this.form.type) {
            case 1:
              //新增的时候直接在追加一个
              const newChild = {
                id: res.data.resultData,
                label: this.form.router.title,
                children: []
              }
              //父级ID不为0则追加子节点，否则添加顶级节点
              if (this.form.router.parentId != 0) {
                this.treeData.children.push(newChild)
              } else {
                this.$refs.tree.root.data.push(newChild)
              }
              this.clearAll()
              break
            case 2:
              //编辑的时候将树上面的显示文字更改一下
              this.treeData.label = this.form.router.title
              break
            case 3:
              this.$refs.tree.remove(this.treeData)
              this.clearAll()
              break
          }
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 单击路由排序
     */
    clickRouterOrder() {
      this.menuList()
    },
    /**
     * 排序列表
     */
    menuList() {
      menuListApi().then(res => {
        if (res.data.resultCode == 1) {
          this.orderDialogVisible = true
          this.orderTableData = res.data.resultData
        } else {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 上移
     */
    clickTopMove(index, row) {
      // index 从0开始
      index++
      if (index - 1 <= 0) {
        this.$message.error('该路由已移至最顶端')
        return
      }
      this.updateSort(index, row.id, 1)
    },
    /**
     * 下移
     */
    clickBottomMove(index, row) {
      //index 从0开始的,现在可以先加一个
      index++
      if (index > this.orderTableData.length) {
        this.$message.error('该路由已移至最底端')
        return
      }
      this.updateSort(index, row.id, 2)
    },
    /**
     * 更新序号
     * @param index 序号
     * @param id id
     * @param type 1 上移 2下移
     */
    updateSort(index, id, type) {
      const param = {
        index, id, type
      }
      updateSortApi(param).then(res => {
        if (res.data.resultCode == 1) {
          this.menuList()
        } else {
          this.$message.error(res.data.resultMessage)
        }
      })
    }, clearAll() {
      this.form.router.id = ''
      this.form.router.name = ''
      this.form.router.title = ''
      this.form.router.path = ''
      this.form.router.component = ''
      this.form.router.redirect = ''
      this.form.router.icon = ''
      this.form.router.hidden = ''
      this.form.router.parentId = ''
      this.form.router.noCache = ''
      this.form.router.describe = ''
      this.form.router.alwaysShow = ''
      this.form.router.activeMenu = ''
      this.form.router.orderIndex = ''
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
