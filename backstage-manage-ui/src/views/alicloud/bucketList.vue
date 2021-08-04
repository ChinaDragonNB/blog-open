<template>
  <div class="app-container">
    <el-table :data="bucketList" style="width: 100%" highlight-current-row @row-click="clickRow"
              :row-style="{cursor: 'pointer'}">
      <el-table-column type="index" width="80" label="序号" align="center"></el-table-column>
      <el-table-column prop="bucketName" label="Bucket名称" align="center"
                       class-name=".tableCell"></el-table-column>
      <el-table-column prop="createDate" label="创建时间" align="center"></el-table-column>
      <el-table-column prop="storageType" label="存储类型" align="center"></el-table-column>
      <el-table-column prop="location" label="数据中心" align="center"></el-table-column>
      <el-table-column prop="extranetEndpoint" label="外网域名" align="center"></el-table-column>
      <el-table-column prop="intranetEndpoint" label="内网域名" align="center"></el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getBucketListApi } from '@/api/alicloud'

export default {
  data() {
    return {
      bucketList: []
    }
  }, created() {
    getBucketListApi().then(res => {
      this.bucketList = res.data.resultData
    })
  }, methods: {
    clickRow(row) {
      this.$router.push('/alicloud/object/' + row.bucketName)
    }
  }
}
</script>

<style scoped>

</style>
