<template>
  <div class="container" v-show="isShow">
    <x-chart id="links" class="high" :option="option"></x-chart>
  </div>
</template>
<script>
// 导入chart组件
import XChart from '@/components/Charts/charts'
import {
  browseChartsApi
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
    this.browseCharts()
  },
  methods: {
    browseCharts() {
      browseChartsApi().then(res => {
        if (res.data.resultCode == 1) {
          this.isShow = true
          this.option = {
            credits: {
              enabled: false
            },
            title: {
              text: '<b>网站每日访问量</b>'
            },
            xAxis: {
              type: 'datetime',
              tickInterval: 2 * 7 * 24 * 3600 * 1000,
              dateTimeLabelFormats: {
                week: '%Y-%m-%d'
              }
            },
            yAxis: {
              title: {
                text: '数量'
              }
            },
            legend: {
              layout: 'vertical', //图例数据项的布局。布局类型： "horizontal" 或 "vertical" 即水平布局和垂直布局 默认是：horizontal.
              align: 'right', //设定图例在图表区中的水平对齐方式，合法值有 left , center 和 right
              verticalAlign: 'middle' //设定图例在图表区中的垂直对齐方式，合法值有 top，middle 和 bottom。垂直位置可以通过 y 选项做进一步设定。
            }, tooltip: {
              shared: true,
              crosshairs: true,
              // 时间格式化字符
              // 默认会根据当前的数据点间隔取对应的值
              // 当前图表中数据点间隔为 1天，所以配置 day 值即可
              dateTimeLabelFormats: {
                hour: '%Y-%m-%d'
              }
            },
            plotOptions: {
              series: {
                label: {
                  connectorAllowed: true
                }
              }
            },
            series: [{
              type: 'area',
              name: '访问量',
              data: res.data.resultData.browseCount
            },
              {
                type: 'area',
                name: '访问人数',
                data: res.data.resultData.browseIp
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
        } else {
          this.isShow = false
          this.$message.error(res.data.resultMessage)
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
