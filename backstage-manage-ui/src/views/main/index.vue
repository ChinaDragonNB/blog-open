<template>
  <div class="dashboard-container">
    <panel></panel>
    <div class="extend">
      <el-row :gutter="24">
        <el-col :xl="16" :lg="24" :md="24" :sm="24" :xs="24">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>最近文章</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="$router.push('/article/list')">更多文章
              </el-button>
            </div>
            <el-row>
              <el-col :xl="8" :lg="8" :md="12" :sm="24" :xs="24" v-for="(item,i) in articleList" :key="item.uuid">
                <el-card shadow="hover" style="border: 1px solid #E8E8E8;border-radius: 0;">
                  <div>
                    <div slot="title" class="card-title">
                      <el-image :src="item.cover" class="card-image"/>
                      <a :href="website+'/article/detail/'+item.uuid" target="_blank">{{ item.title }}</a>
                    </div>
                    <div slot="description" class="card-description">
                      {{ item.desc }}
                    </div>
                  </div>
                  <div class="project-item">
                    <a :href="website+'/article?tag='+item.tag" target="_blank">{{ item.tag }}</a>
                    <span class="datetime">{{ item.date }}</span>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
        <el-col style="padding: 0 12px" :xl="8" :lg="24" :md="24" :sm="24" :xs="24">
          <el-card title="" style="margin-bottom: 24px">
            <div slot="header" class="clearfix">
              <span>实用网站</span>
            </div>
            <div class="item-group">
              <a :href="item.websiteUrl" v-for="(item,i) in websiteTools" :key="item.id" target="_blank" :title="item.desc">{{ item.websiteName }}</a>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style scoped>
.dashboard-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;
}

.card-title {
  margin-bottom: 8px;
}

.card-title a {
  color: rgba(0, 0, 0, 0.85);
  margin-left: 10px;
  line-height: 40px;
  height: 40px;
  display: inline-block;
  vertical-align: top;
  font-size: 14px;
  overflow: hidden;
}

.card-title a:hover, .project-item a:hover, .item-group a:hover {
  color: #1890ff;
}

.card-image {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.card-description {
  color: rgba(0, 0, 0, 0.45);
  height: 44px;
  line-height: 22px;
  overflow: hidden;
  font-size: 14px;
}

.item-group a {
  color: rgba(0, 0, 0, 0.65);
  display: inline-block;
  font-size: 14px;
  margin-bottom: 13px;
  width: 25%;
}


.project-item {
  display: flex;
  margin-top: 8px;
  overflow: hidden;
  font-size: 12px;
  height: 20px;
  line-height: 20px;

}

.project-item a {
  color: rgba(0, 0, 0, 0.45);
  display: inline-block;
  flex: 1 1 0;
}

.project-item .datetime {
  color: rgba(0, 0, 0, 0.25);
  flex: 0 0 auto;
  float: right;

}

.item-group {
  padding: 20px 0 8px 24px;
  font-size: 0;
}

.item-group a {
  color: rgba(0, 0, 0, 0.65);
  display: inline-block;
  font-size: 14px;
  margin-bottom: 13px;
  width: 24%;
  overflow: hidden;
}

.extend {
  margin-top: 30px;
}

</style>
<script>
import panel from './panel'
import {
  getWebsiteToolsApi, getLatelyArticleApi
} from '@/api/main'

export default {
  data() {
    return {
      articleList: [],
      website: '',
      websiteTools: []
    }
  },
  name: 'Main',
  components: {
    panel
  },
  mounted() {
    this.website = localStorage.getItem('website')

    this.getWebsiteTools()
    this.getLatelyArticle()

  },
  methods: {
    getWebsiteTools() {
      getWebsiteToolsApi().then(res => {
        this.websiteTools = res.data

      })
    },
    getLatelyArticle() {
      getLatelyArticleApi().then(res => {
        if (res.data.resultCode == 1) {
          this.articleList = res.data.resultData
        }
      })
    }

  }
}
</script>
