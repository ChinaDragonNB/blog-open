<template>
  <div class="container" v-show="isShow">
    <x-chart id="tag" class="high" :option="option"></x-chart>
  </div>
</template>
<script>
// 导入chart组件
import XChart from '@/components/Charts/charts'
import {
  tagColumnApi
} from '@/api/main'

export default {
  components: {
    XChart
  },
  data() {
    return {
      isShow: true,
      option: {}
    }
  },
  created() {
    this.tagColumn()
  },
  methods: {
    tagColumn() {
      tagColumnApi().then(res => {
        this.isShow = true
        let data = []
        if (res.data.resultCode == 1) {

          data = res.data.resultData
        }
        this.option = {
          credits: {
            enabled: false
          },
          chart: {
            type: 'column'
          },
          title: {
            text: '<b>标签使用次数Top 10</b>'
          },
          xAxis: {
            type: 'category',
            labels: {
              rotation: -45 // 设置轴标签旋转角度
            }
          },
          yAxis: {
            min: 0,
            title: {
              text: '使用次数'
            }
          },
          legend: {
            enabled: false
          },
          tooltip: {
            pointFormat: ''
          },
          plotOptions: {
            column: {
              borderWidth: 0
            }
          },
          series: [{
            colorByPoint: true,
            data: data,
            dataLabels: {
              enabled: true,
              // rotation: -90,
              color: '#000',
              // align: 'right',
              format: '{point.y}' // :.1f 为保留 1 位小数
              // y: 10
            }
          }]
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
