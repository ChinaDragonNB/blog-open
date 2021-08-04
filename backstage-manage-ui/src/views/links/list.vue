<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :xs="24" :sm="12" :md="4" :lg="4" :xl="4">
        <el-input v-model="searchKey" placeholder="请输入网站标题" size="small" clearable></el-input>
      </el-col>
      <el-col :xs="24" :sm="12" :md="20" :lg="20" :xl="20">
        <el-button @click="clickSearch" icon="el-icon-search" size="small" circle></el-button>
        <el-button @click="clickSearch" size="small" icon="el-icon-refresh" circle></el-button>

      </el-col>
    </el-row>
    <br/>
    <el-table v-loading="listLoading" :data="list" element-loading-text="加载中..." highlight-current-row>
      <el-table-column type="index" label="序号" width="80" align="center">
        <template slot-scope="scope"><span>{{ scope.$index + (pageNum - 1) * pageSize + 1 }} </span></template>
      </el-table-column>
      <el-table-column label="网站名称" align="center">
        <template slot-scope="scope">{{ scope.row.title }}</template>
      </el-table-column>
      <el-table-column label="网站链接" align="center">
        <template slot-scope="scope">
          <el-link :href="scope.row.link" type="primary" :underline="false" target="_blank">{{ scope.row.link }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column label="网站Logo" align="center">
        <template slot-scope="scope">
          <img :src="scope.row.logo" width="50" height="50" style="border-radius: 50%"/>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="审核状态" align="center">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.checkStatus==2">已通过</el-tag>
          <el-tag type="warning" v-else>未知</el-tag>
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
    <el-dialog :title="dialog.dialogTitle" :visible.sync="dialog.dialogVisible" width="500px" :close-on-click-modal="false">
      <el-form :model="dialog">
        <el-form-item label="网站名称" :label-width="dialog.formLabelWidth">
          <el-input size="small" v-model="dialog.title" autocomplete="off" :disabled="dialog.isView"></el-input>
        </el-form-item>
        <el-form-item label="网站链接" :label-width="dialog.formLabelWidth">
          <el-input size="small" v-model="dialog.link" autocomplete="off" :disabled="dialog.isView"></el-input>
        </el-form-item>
        <el-form-item label="网站Logo" :label-width="dialog.formLabelWidth">
          <el-input size="small" v-model="dialog.logo" autocomplete="off" :disabled="dialog.isView"></el-input>
          <img :src="dialog.logo" width="80" height="80" style="margin-top: 20px;border-radius: 50%"/>
        </el-form-item>
        <el-form-item label="联系邮箱" :label-width="dialog.formLabelWidth">
          <el-input size="small" v-model="dialog.email" autocomplete="off" :disabled="dialog.isView"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" type="primary" v-if="dialog.isView==false" @click="clickSave">保存</el-button>
        <el-button size="small" @click="dialog.dialogVisible = false">返回</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  sendPostJSON,
  sendPostParams
} from '@/utils/sendRequest'
import { delLinkApi, editLinkApi, linkInfoApi, listLinkApi } from '@/api/links'

export default {
  data() {
    return {
      list: null, //数据集合
      listLoading: true, //加载
      pageNum: 1, //当前页
      pageSize: 10, //每页显示多少条数据
      total: 0, //总条数
      prePage: 0, //上一页
      nextPage: 0, //下一页
      pages: 0, //总页数
      dialog: {
        dialogVisible: false, //查看窗口是否显示
        dialogTitle: '', //窗口标题
        id: 0,
        title: '', //网站标题
        link: '', //网站链接
        logo: '', //网站logo
        email: '', //网站联系邮箱
        formLabelWidth: '80px', //显示宽度
        isView: true //是否为查看 false 为编辑，true为查看
      }, searchKey: ''
    }
  },
  created() {
    this.listLink()
  },
  methods: {
    listLink() {
      this.listLoading = true
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        title: this.searchKey,
        type: 1
      }
      listLinkApi(params).then(res => {
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
      this.listLink()
    },
    prevClick() {
      this.pageNum = this.prePage
      this.listLink()
    },
    currentChange(pageNum) {
      this.pageNum = pageNum
      this.listLink()
    },
    linkInfo(id) {
      linkInfoApi(id).then(res => {
        if (res.data.resultCode == 1) {
          this.dialog.title = res.data.resultData.title
          this.dialog.email = res.data.resultData.email
          this.dialog.link = res.data.resultData.link
          this.dialog.logo = res.data.resultData.logo
          this.dialog.dialogVisible = true
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 单击查看
     */
    clickView(index, row) {
      this.dialog.dialogTitle = '查看'
      this.dialog.isView = true
      const id = row.id
      this.dialog.id = id
      this.linkInfo(id)

    },
    clickEdit(index, row) {
      this.dialog.dialogTitle = '编辑'
      this.dialog.isView = false
      const id = row.id
      this.dialog.id = id
      this.linkInfo(id)
    },
    clickSave() {
      const params = {
        id: this.dialog.id,
        title: this.dialog.title,
        link: this.dialog.link,
        logo: this.dialog.logo,
        email: this.dialog.email
      }
      editLinkApi(params).then(res => {
        if (res.data.resultCode == 1) {
          this.$message.success(res.data.resultMessage)
          this.listLink()
          this.dialog.isView = false
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    clickDel(index, row) {
      this.$confirm('确定删除该链接吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delLinkApi(row.id).then(res => {
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
    }, clickSearch() {
      this.listLink()
    }
  }
}
</script>
