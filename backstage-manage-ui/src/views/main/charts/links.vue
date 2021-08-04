<template>
  <div class="container" v-show="isShow">
    <x-chart id="links" class="high" :option="option"></x-chart>
  </div>
</template>
<script>
// 导入chart组件
import XChart from '@/components/Charts/charts'
import {
  linkLineApi
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
    this.linkLine()
  },
  methods: {
    linkLine() {
      linkLineApi().then(res => {
        this.isShow = true
        let resultData = []
        if (res.data.resultCode == 1) {
          resultData = res.data.resultData

        }
        this.option = {
          credits: {
            enabled: false
          },
          title: {
            text: resultData.title
          },
          xAxis: {
            allowDecimals: true,
            categories: [
              '一月',
              '二月',
              '三月',
              '四月',
              '五月',
              '六月',
              '七月',
              '八月',
              '九月',
              '十月',
              '十一月',
              '十二月'
            ]
          },
          yAxis: {
            title: {
              text: '申请人数'
            }
          },
          legend: {
            layout: 'vertical', //图例数据项的布局。布局类型： "horizontal" 或 "vertical" 即水平布局和垂直布局 默认是：horizontal.
            align: 'center', //设定图例在图表区中的水平对齐方式，合法值有 left , center 和 right
            verticalAlign: 'top' //设定图例在图表区中的垂直对齐方式，合法值有 top，middle 和 bottom。垂直位置可以通过 y 选项做进一步设定。
          },
          plotOptions: {
            series: {
              label: {
                connectorAllowed: false
              }
            }
          },
          series: [{
            color: '#67C23A',
            name: '通过人数',
            data: resultData.pass
          },
            {
              color: '#F56C6C',
              name: '未通过人数',
              data: resultData.noPass
            }
          ],
          responsive: {
            rules: [{
              condition: {
                maxWidth: 500
              },
              chartOptions: {
                legend: {
                  layout: 'horizontal',
                  align: 'right',
                  verticalAlign: 'bottom'
                }
              }
            }]
          }
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
