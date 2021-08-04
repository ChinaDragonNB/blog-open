<template>
  <div class="container">
    <el-row :gutter="30">
      <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
        <el-button @click="tree" size="small" icon="el-icon-refresh">刷新</el-button>
        <el-button size="small" @click="appendTop" type="primary" icon="el-icon-key">添加父级权限</el-button>
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
        <el-form ref="form" :model="form" label-width="100px" v-show="form.isShow">
          <el-form-item label="权限英文名称">
            <el-input v-model="form.authorityNameEn" size="small"></el-input>
          </el-form-item>
          <el-form-item label="权限中文名称">
            <el-input v-model="form.authorityNameCn" size="small"></el-input>
          </el-form-item>
          <el-form-item label="状态">
            <el-switch v-model="form.state" active-color="#13ce66" inactive-color="#ff4949" active-text="正常" inactive-text="锁定"></el-switch>
          </el-form-item>
          <el-form-item label="拥有接口">
            <el-select v-model="checkApiList" placeholder="请选择接口" size="small" multiple clearable>
              <el-option-group v-for="api in apiList" :key="api.id" :label="api.description">
                <el-option v-for="item in api.children" :key="item.id" :label="item.description" :value="item.id">
                  <span style="float: left;">{{ item.description }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px;margin-left:20px">{{ item.apiUrl }}</span>
                </el-option>
              </el-option-group>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit" size="small">保存</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import { apiListApi, authorityInfoApi, saveApi, treeApi } from '../../api/authority'

export default {
  data() {
    return {
      data: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      treeData: [],
      form: {
        isShow: false, //表单是否显示
        type: '', //提交类型
        id: '', //权限ID
        parentId: '', //父级权限ID
        authorityNameEn: '', //权限英文名
        authorityNameCn: '', //权限中文名
        state: true //权限状态
      },
      apiList: [], // 接口列表
      checkApiList: [] // 默认选中的接口
    }
  },
  created() {
    this.tree()
    this.listApi()
  },
  methods: {
    /**
     * 查询权限树
     */
    tree() {
      treeApi().then(res => {
        if (res.data.resultCode == 1) {
          this.data = res.data.resultData
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 根据ID查询数据
     */
    authorityInfo() {
      authorityInfoApi(this.form.id).then(res => {
        if (res.data.resultCode == 1) {
          this.form.authorityNameEn = res.data.resultData.authorityNameEn
          this.form.authorityNameCn = res.data.resultData.authorityNameCn
          this.form.state = res.data.resultData.state
          this.checkApiList = res.data.resultData.apiIds
        } else {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 单击任意节点时
     */
    handleNodeClick(data) {
      this.checkApiList = []
      this.treeData = data
      this.form.isShow = true
      this.form.type = 2
      //当前单击的节点有子节点的话
      this.form.id = data.id
      //编辑的时候不需要父级节点
      this.form.parentId = null
      this.authorityInfo()
    },
    /**
     * 单击添加顶级节点按钮时
     */
    appendTop() {
      this.treeData = []
      this.form.isShow = true
      this.form.type = 1
      //顶级节点父级ID默认为0
      this.form.parentId = 0
      this.form.authorityNameCn = ''
      this.form.authorityNameEn = ''
      this.form.state = true
      this.checkApiList = []
    },
    /**
     * 单击添加按钮时
     */
    append(data) {
      this.treeData = data
      this.form.isShow = true
      this.form.type = 1
      //当前节点ID为子节点的父ID
      this.form.parentId = data.id
      this.form.authorityNameCn = ''
      this.form.authorityNameEn = ''
      this.form.state = true
      this.checkApiList = []
    },
    /**
     * 单击移除按钮时
     */
    remove(node, data) {
      this.treeData = data
      this.form.id = data.id
      this.form.type = 3
      if (data.children != null) {
        if (data.children.length != 0) {
          this.$message.error('不能删除该节点,该节点下还有子节点')
          return
        }
      }
      this.$confirm('确定删除该权限吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.save(3, this.form.id)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    /**
     * 提交数据
     */
    onSubmit() {
      if (this.form.authorityNameEn === '') {
        this.$message.error('请输入权限英文名称')
        return
      }
      if (this.form.authorityNameCn === '') {
        this.$message.error('请输入权限中文名称')
        return
      }
      if (!this.form.state && this.treeData.children != null) {
        this.$message.error('不能禁用该节点,该节点下还有子节点')
        return
      }
      const params = {
        authority: {
          id: this.form.id,
          parentId: this.form.parentId,
          authorityNameEn: this.form.authorityNameEn,
          authorityNameCn: this.form.authorityNameCn,
          state: this.form.state
        },
        apiIds: this.checkApiList
      }
      this.save(this.form.type, params)
    },
    /**
     * 执行请求
     */
    save(type, params) {
      saveApi(type, params).then(res => {
        if (res.data.resultCode == 1) {
          this.$message.success(res.data.resultMessage)
          switch (this.form.type) {
            case 1:
              //新增的时候直接在追加一个
              const newChild = {
                id: res.data.resultData,
                label: this.form.authorityNameCn,
                children: []
              }
              //父级ID不为0则追加子节点，否则添加顶级节点
              if (this.form.parentId !== 0) {
                this.treeData.children.push(newChild)
              } else {
                this.$refs.tree.root.data.push(newChild)
              }
              this.clearAll()
              break
            case 2:
              //编辑的时候将树上面的显示文字更改一下
              this.treeData.label = this.form.authorityNameCn
              break
            case 3:
              this.$refs.tree.remove(this.treeData)
              this.clearAll()
              break
          }
        } else if (res.data.resultCode === -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 接口列表
     */
    listApi() {
      apiListApi().then(res => {
        if (res.data.resultCode === 1) {
          this.apiList = res.data.resultData
        } else {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    clearAll() {
      this.form.isShow = false
      this.form.type = ''
      this.form.id = ''
      this.form.parentId = ''
      this.form.authorityNameEn = ''
      this.form.authorityNameCn = ''
      this.form.state = true
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
