<template>
  <div class="container" v-show="isShow">
    <x-chart id="article" class="high" :option="option"></x-chart>
  </div>
</template>
<script>
// 导入chart组件
import XChart from '@/components/Charts/charts'
import {
  articlePieApi
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
    this.articlePie()
  },
  methods: {
    articlePie() {
      articlePieApi().then(res => {
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
            type: 'pie',
            options3d: {
              enabled: true,
              alpha: 45
              // beta: 0
            }
          },
          title: {
            text: '<b>文章访问量Top10</b>'
          },
          tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
          },
          plotOptions: {
            pie: {
              innerSize: 100,
              allowPointSelect: true,
              cursor: 'pointer',
              depth: 45,
              slicedOffset: 40,
              startAngle: 300,
              dataLabels: {
                enabled: true,
                format: '<b>{point.name}</b>: {y} 次'
              }
            }
          },
          series: [{
            name: '占比',
            size: '100%',
            colorByPoint: true,
            data: data
          }]
        }
      })
    }
  }
}
</script>
<style scoped>
.container {
  width: 100%;
  height: 100%;
}
</style>
