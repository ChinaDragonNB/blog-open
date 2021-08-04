<template>
  <div class="block">
    <el-table :data="pageInfo.list" highlight-current-row>
      <el-table-column type="index" label="序号" width="100" align="center">
        <template slot-scope="scope"><span>{{ scope.$index + (pageInfo.pageNum - 1) * pageInfo.pageSize + 1 }} </span></template>
      </el-table-column>
      <el-table-column label="文章标题" align="center">
        <template slot-scope="scope">
          <span class="article">{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column label="IP地址" align="center">
        <template slot-scope="scope">
          <span class="ip location">{{ scope.row.browseIp }}</span>
        </template>
      </el-table-column>
      <el-table-column label="地理位置" align="center">
        <template slot-scope="scope">
          <span class="location">{{ scope.row.ipLocation }}</span>
        </template>
      </el-table-column>
      <el-table-column label="访问时间" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time"/>
          {{ scope.row.browseTime }}
        </template>
      </el-table-column>

    </el-table>
    <br>
    <el-pagination background layout="prev, pager, next" :total="pageInfo.total" :page-size="pageInfo.pageSize"
                   @next-click="nextClick" @prev-click="prevClick" @current-change="currentChange"></el-pagination>
  </div>
</template>

<script>

import { articleLogsApi } from '@/api/log'

export default {
  name: 'browseRecord',
  data() {
    return {
      pageInfo: {
        pageNum: 1, // 当前页
        pageSize: 20, // 每页显示的数量
        total: 0, // 总数量
        list: [], // 数据
        prePage: 0, // 上一页
        nextPage: 0 // 下一页
      }
    }
  },
  created() {
    this.init()
  },
  methods: {
    /**
     * 初始化方法
     */
    init() {
      this.listArticleLog()
    },
    listArticleLog() {
      const params = {
        pageNum: this.pageInfo.pageNum,
        pageSize: this.pageInfo.pageSize
      }
      articleLogsApi(params).then(res => {
        if (res.data.resultCode == 1) {
          this.pageInfo = res.data.resultData
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 下一页
     */
    nextClick() {
      this.pageInfo.pageNum = this.pageInfo.nextPage
      this.listArticleLog()
    },
    /**
     * 上一页
     */
    prevClick() {
      this.pageInfo.pageNum = this.pageInfo.prePage
      this.listArticleLog()
    },
    /**
     * 当页数改变时
     * @param pageNum 当前页
     */
    currentChange(pageNum) {
      this.pageInfo.pageNum = pageNum
      this.listArticleLog()
    }
  }
}
</script>

<style scoped>
.block {
  padding: 10px;
}

.location {
  font-weight: bold;
  color: #409EFF;
}

/*.time {*/
/*    font-weight: bold;*/
/*    color: #E6A23C;*/
/*}*/

/*.article {*/
/*    font-weight: bold;*/
/*    color: #67C23A;*/
/*}*/

/*.ip {*/
/*    font-weight: bold;*/
/*    color: #F56C6C;*/
/*}*/

</style>
