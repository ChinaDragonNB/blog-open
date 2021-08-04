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
          <a :href="scope.row.link" style="color: #218EF5;" target="_blank">{{ scope.row.link }}</a>
        </template>
      </el-table-column>
      <el-table-column label="网站Logo" align="center">
        <template slot-scope="scope">
          <img :src="scope.row.logo" width="50" height="50" style="border-radius: 50%"/>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="审核状态" align="center">
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.checkStatus==0">未通过</el-tag>
          <el-tag type="warning" v-if="scope.row.checkStatus==1">未审核</el-tag>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="success" v-if="scope.row.checkStatus==1" @click="checkLink(scope.$index, scope.row,2)" icon="el-icon-check" size="small" circle></el-button>
          <el-button type="danger" v-if="scope.row.checkStatus==1" @click="checkLink(scope.$index, scope.row,0)" icon="el-icon-close" size="small" circle></el-button>
          <el-button type="info" v-if="scope.row.checkStatus==0" @click="checkLink(scope.$index, scope.row,1)" size="small">重新审核</el-button>
        </template>
      </el-table-column>
    </el-table>
    <br/>
    <el-pagination background layout="prev, pager, next" :total="total" @next-click="nextClick" @current-change="currentChange" @prev-click="prevClick"></el-pagination>
  </div>
</template>

<script>

import { checkLinkApi, listLinkApi } from '@/api/links'

export default {
  data() {
    return {
      list: null, //文章数据集合
      listLoading: true, //加载
      pageNum: 1, //当前页
      pageSize: 10, //每页显示多少条数据
      total: 0, //总条数
      prePage: 0, //上一页
      nextPage: 0, //下一页
      searchKey: ''
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
        type: 2
      }
      listLinkApi(params).then(res => {
        if (res.data.resultCode == 1) {
          this.list = res.data.resultData.list
          this.total = res.data.resultData.total
          this.pageNum = res.data.resultData.pageNum
          this.pageSize = res.data.resultData.pageSize
          this.prePage = res.data.resultData.prePage
          this.nextPage = res.data.resultData.nextPage
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
    checkLink(index, row, result) {
      const id = row.id
      this.$confirm('确认审核该链接吗?', '审核提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const params = {
          id,
          result
        }
        checkLinkApi(params).then(res => {
          if (res.data.resultCode == 1) {
            this.$message.success(res.data.resultMessage)
            this.listLink()
          } else if (res.data.resultCode == -1) {
            this.$message.error(res.data.resultMessage)
          }
        })
      }).catch(() => {
        this.$message.info('已取消审核')
      })
    }, clickSearch() {
      this.listLink()
    }
  }
}
</script>
