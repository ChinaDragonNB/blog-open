<template>
  <div class="container">
    <el-row :gutter="40" class="panel-group">
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="articleMouse()">
          <div class="card-panel-icon-wrapper icon-people">
            <svg-icon icon-class="panel-article" class-name="card-panel-icon"/>
          </div>
          <div class="card-panel-description" align="center">
            <div class="card-panel-text">文章</div>
            <count-to :start-val="0" :end-val="statistics.articleNum" :duration="3200" class="card-panel-num"/>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="tagMouse()">
          <div class="card-panel-icon-wrapper icon-message">
            <svg-icon icon-class="panel-tags" class-name="card-panel-icon"/>
          </div>
          <div class="card-panel-description" align="center">
            <div class="card-panel-text">标签</div>
            <count-to :start-val="0" :end-val="statistics.tagsNum" :duration="3200" class="card-panel-num"/>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="linkMouse()">
          <div class="card-panel-icon-wrapper icon-money">
            <svg-icon icon-class="panel-links" class-name="card-panel-icon"/>
          </div>
          <div class="card-panel-description" align="center">
            <div class="card-panel-text">友情链接</div>
            <count-to :start-val="0" :end-val="statistics.linksNum" :duration="3200" class="card-panel-num"/>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="browseCountMouse()">
          <div class="card-panel-icon-wrapper icon-shopping">
            <svg-icon icon-class="panel-views" class-name="card-panel-icon"/>
          </div>
          <div class="card-panel-description" align="center">
            <div class="card-panel-text">总访问量</div>
            <count-to :start-val="0" :end-val="statistics.viewsSum" :duration="3200" class="card-panel-num"/>
          </div>
        </div>
      </el-col>
    </el-row>
    <Article v-if="articleChartsShow"></Article>
    <Tag v-if="tagChartsShow"></Tag>
    <Link v-if="linkChartsShow"></Link>
    <BrowseCount v-if="browseCountChartsShow"></BrowseCount>
  </div>
</template>

<script>
import CountTo from 'vue-count-to'
import Article from '@/views/main/charts/article'
import Tag from '@/views/main/charts/tag'
import Link from '@/views/main/charts/links'
import BrowseCount from '@/views/main/charts/browseCount'
import {
  statisticsApi
} from '@/api/main'

export default {
  name: 'panel',
  data() {
    return {
      statistics: {
        articleNum: 0,
        tagsNum: 0,
        linksNum: 0,
        viewsSum: 0
      },
      articleChartsShow: false,
      tagChartsShow: false,
      linkChartsShow: false,
      browseCountChartsShow: true
    }
  },
  mounted() {
    this.getStatistics()
  },
  components: {
    CountTo,
    Article,
    Tag,
    Link,
    BrowseCount
  },
  methods: {
    //获取各个模块的数量
    getStatistics() {
      statisticsApi().then(res => {
        if (res.data.resultCode == 1) {
          this.statistics = res.data.resultData
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 文章面板
     */
    articleMouse() {
      this.articleChartsShow = true
      this.tagChartsShow = false
      this.linkChartsShow = false
      this.browseCountChartsShow = false
    },
    /**
     * 标签面板
     */
    tagMouse() {
      this.articleChartsShow = false
      this.tagChartsShow = true
      this.linkChartsShow = false
      this.browseCountChartsShow = false
    },
    /**
     * 友情链接面板
     */
    linkMouse() {
      this.articleChartsShow = false
      this.tagChartsShow = false
      this.linkChartsShow = true
      this.browseCountChartsShow = false

    },
    /**
     * 总访问数面板
     */
    browseCountMouse() {
      this.articleChartsShow = false
      this.tagChartsShow = false
      this.linkChartsShow = false
      this.browseCountChartsShow = true
    }

  }
}
</script>

<style lang="scss" scoped>

.panel-group {
  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #FFFFFF;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, 0.1);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3;
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 10px;
      transition: all 0.38s ease-out;
      border-radius: 10px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width: 550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
