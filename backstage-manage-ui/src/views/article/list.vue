<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :xs="24" :sm="24" :md="4" :lg="4" :xl="4">
        <el-input v-model="searchKey" placeholder="请输入文章标题" size="small" clearable></el-input>
      </el-col>
      <el-col :xs="24" :sm="24" :md="20" :lg="20" :xl="20">
        <el-date-picker placeholder="请输入文章标题" v-model="publishTime" type="daterange" size="small"
                        :picker-options="pickerOptions" range-separator="-" start-placeholder="发布时间"
                        end-placeholder="发布时间" align="center">
        </el-date-picker>
        <el-button @click="clickSearch" icon="el-icon-search" size="small" circle></el-button>
        <el-button @click="clickSearch" size="small" icon="el-icon-refresh" circle></el-button>
        <el-button @click="clickExport" size="small" type="primary" icon="el-icon-upload2">导出</el-button>
        <el-upload :http-request="uploadMarkDown" action="" :show-file-list="false" style="display: inline">
          <el-button size="small" type="primary" icon="el-icon-download">导入
          </el-button>
        </el-upload>
      </el-col>
    </el-row>
    <br/>
    <el-table v-loading="listLoading" :data="list" element-loading-text="加载中..." highlight-current-row @sort-change="sortChange">
      <el-table-column type="index" label="序号" align="center">
        <template slot-scope="scope"><span>{{ scope.$index + (pageNum - 1) * pageSize + 1 }} </span></template>
      </el-table-column>
      <el-table-column label="标题" align="center" sortable="custom" prop="title">
        <template slot-scope="scope">
          <span style>{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="阅读数" align="center" sortable="custom" prop="views">
        <template slot-scope="scope">
          <i class="el-icon-view"/>
          {{ scope.row.views }}
        </template>
      </el-table-column>
      <el-table-column label="标签" align="center" prop="tag_id">
        <template slot-scope="scope">
          <img :src="scope.row.tagLogo" width="28" height="28"/>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" sortable="custom" prop="state">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.state==1" type="success">公开</el-tag>
          <el-tag v-if="scope.row.state==2" type="info">私密</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="发布时间" sortable="custom" prop="publish_time">
        <template slot-scope="scope">
          <i class="el-icon-time"/>
          <span>{{ scope.row.publishTime }}</span>
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
  </div>
</template>

<script>


import { delArticleApi, listArticleApi, replaceArticleContentApi, uploadMarkDownApi } from '../../api/article'

export default {
  data() {
    return {
      columnName: '', // 排序的列名
      order: '', // 排序方式
      searchKey: '',// 搜索关键字
      publishTime: '', // 发布时间查询存储
      publishTimeStart: '', // 发布时间开始
      publishTimeEnd: '',// 发布时间结尾
      list: null, // 数据集合
      listLoading: true, // 加载
      pageNum: 1, // 当前页
      pageSize: 15, // 每页显示多少条数据
      total: 0, // 总条数
      prePage: 0, // 上一页
      nextPage: 0, // 下一页
      pages: 0, // 总页数
      pickerOptions: {
        shortcuts: [{
          text: '三天内',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 3)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        },
          {
            text: '最近半年',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 181)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近一年',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 365)
              picker.$emit('pick', [start, end])
            }
          }]
      }
    }
  },
  created() {
    this.listArticle()
  },
  methods: {
    /**
     *单击查询按钮
     */
    clickSearch() {
      if (this.publishTime != '' && this.publishTime != null) {
        // 时间格式化
        this.publishTimeStart = this.dateFormat('YYYY-mm-dd', this.publishTime[0])
        this.publishTimeEnd = this.dateFormat('YYYY-mm-dd', this.publishTime[1])
      }
      this.listArticle()
    },
    /**
     * 排序方式
     */
    sortChange(data) {
      this.columnName = data.prop
      this.order = data.order
      this.listArticle()
    },
    listArticle() {
      this.listLoading = true
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        title: this.searchKey,
        columnName: this.columnName,
        order: this.order,
        publishTimeStart: this.publishTimeStart,
        publishTimeEnd: this.publishTimeEnd
      }
      listArticleApi(params).then(res => {
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
      this.listArticle()
    },
    prevClick() {
      this.pageNum = this.prePage
      this.listArticle()
    },
    currentChange(pageNum) {
      this.pageNum = pageNum
      this.listArticle()
    },
    clickView(index, row) {
      this.$router.push({
        path: '/article/view/' + row.id
      })
    },
    clickEdit(index, row) {
      this.$router.push({
        path: '/article/edit/' + row.id
      })
    },
    clickDel(index, row) {
      this.$confirm('确定删除该文章吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delArticleApi(row.id).then(res => {
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
    },
    /**
     * 时间格式化
     * @param fmt 格式化
     * @param date 时间
     * @returns {*}
     */
    dateFormat(fmt, date) {
      let ret
      let opt = {
        'Y+': date.getFullYear().toString(),        // 年
        'm+': (date.getMonth() + 1).toString(),     // 月
        'd+': date.getDate().toString(),            // 日
        'H+': date.getHours().toString(),           // 时
        'M+': date.getMinutes().toString(),         // 分
        'S+': date.getSeconds().toString()          // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
      }
      for (let k in opt) {
        ret = new RegExp('(' + k + ')').exec(fmt)
        if (ret) {
          fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, '0')))
        }

      }
      return fmt
    },
    /**
     * 单击导出按钮
     */
    clickExport() {
      window.location.href = process.env.VUE_APP_BASE_API + '/article/exportAll'
    },
    /**
     * 导入
     */
    uploadMarkDown($file) {
      const fileName = $file.file.name
      const fileType = fileName.substr(fileName.lastIndexOf('.') + 1)
      if (fileType) {
        if (fileType != 'md') {
          this.$message.error('导入失败,请上传MarkDown文件')
          return
        }
      } else {
        this.$message.error('导入失败,请上传MarkDown文件')
        return
      }
      const formData = new FormData()
      formData.append('file', $file.file)
      uploadMarkDownApi(formData).then(res => {
        if (res.data.resultCode == 1) {
          if (res.data.resultData) {
            this.$confirm(res.data.resultMessage, '提示', {
              distinguishCancelAndClose: true,
              confirmButtonText: '是',
              cancelButtonText: '否'
            }).then(() => {

              replaceArticleContentApi(res.data.resultData).then(res => {
                if (res.data.resultCode == 1) {
                  this.$message.success(res.data.resultMessage)
                  this.listArticle()
                } else if (res.data.resultCode == -1) {
                  this.$message.error(res.data.resultMessage)
                }
              })
            }).catch(action => {
              this.$message({ type: 'info', message: '已取消导入' })
            })
          } else {
            this.$message.success(res.data.resultMessage)
            this.listArticle()
          }

        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    }

  }
}
</script>


<style>
</style>
