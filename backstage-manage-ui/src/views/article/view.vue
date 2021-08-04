<template>
  <div class="app-container">
    <el-form ref="form" :model="form" class="boxShadow">
      <el-row>
        <el-col :xl="10" :lg="10" :md="10" :sm="10" :xs="24" align="left">
          <el-form-item label="文章标题：">
            <el-input v-model="form.title" placeholder="请输入标题" clearable size="small" style="width:280px;" autofocus
                      disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :xl="10" :lg="10" :md="10" :sm="10" :xs="24">
          <el-form-item label="所属分类：">
            <el-select v-model="form.tagId" placeholder="请选择所属分类" size="small" disabled>
              <el-option v-for="item in form.options" :key="item.value" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
            <!-- ---------- 选择标签嵌入框 start  ---------- -->
            <el-popover placement="top-end" trigger="click" width="550">
              <el-checkbox-group v-model="form.checkOks" size="small" :max="3" disabled
                                 @change="form.isCheckOks=true">
                <el-row align="left">
                  <el-col :md="8" style="padding:5px;" v-for="item in form.tagList" :key="item.tagId">
                    <el-checkbox :label="item.tagId" border>{{ item.tagName }}</el-checkbox>
                  </el-col>
                </el-row>
              </el-checkbox-group>
              <el-button slot="reference" size="small">选择标签</el-button>
            </el-popover>
            <!-- ---------- 选择标签嵌入框 end  ---------- -->
          </el-form-item>
        </el-col>
        <el-col :xl="4" :lg="4" :md="4" :sm="4" :xs="2" align="right">
          <el-button size="small" type="primary" @click="clickExport" icon="el-icon-upload2">导出</el-button>
          <el-button size="small" @click="clickBack">返回</el-button>
        </el-col>
      </el-row>
      <br>
      <br>
      <el-row>
        <el-col :xl="8" :lg="8" :md="8" :sm="8" :xs="24" align="left">
          <span style="font-size:14px;font-weight:bold;color:#606266;">文章类型：</span>
          <el-radio v-model="form.articleType" label="1">
            <span style="font-size:14px;">原创</span>
          </el-radio>
          <el-radio v-model="form.articleType" label="2">
            <span style="font-size:14px;">转载</span>
          </el-radio>
          <el-radio v-model="form.articleType" label="3">
            <span style="font-size:14px;">翻译</span>
          </el-radio>
        </el-col>
        <el-col :xl="8" :lg="8" :md="8" :sm="8" :xs="24" align="left">
          <span style="font-size:14px;font-weight:bold;color:#606266;">文章状态：</span>
          <el-radio v-model="form.state" label="1">
            <span style="font-size:14px;">公开</span>
          </el-radio>
          <el-radio v-model="form.state" label="2">
            <span style="font-size:14px;">私密</span>
          </el-radio>
        </el-col>
        <el-col :xl="8" :lg="8" :md="8" :sm="8" :xs="24" align="left">
          <span style="font-size:14px;font-weight:bold;color:#606266;">是否置顶：</span>
          <el-radio v-model="form.isStickie" label="1">
            <span style="font-size:14px;">是</span>
          </el-radio>
          <el-radio v-model="form.isStickie" label="0">
            <span style="font-size:14px;">否</span>
          </el-radio>
        </el-col>
      </el-row>
      <br/>
      <el-form-item label="文章描述">
        <el-input type="textarea" :rows="6" placeholder="请输入文章描述" v-model="form.describe" disabled
                  clearable></el-input>
      </el-form-item>
      <el-form-item label="文章封面">
        <br>
        <el-input v-model="form.cover" placeholder="请输入封面链接" size="small" style="width:50%;"
                  disabled/>
        <img v-if="form.cover" :src="form.cover" class="avatar" alt=""/>
      </el-form-item>
      <el-row>
        <el-col :md="24" :xl="24" :lg="24" :xs="24">
          <br>
          <br>
          <span style="font-size:14px;font-weight:bold;color:#606266;">文章内容</span>
          <br>
          <br>
          <div class="mavonEditor">
            <mavon-editor ref=md v-model="form.markdown" style="font-size: 14px" :style="zIndex"
                          :editorOption="editorOption" :value="form.markdown"
                          :fontSize="editorOption.fontSize"
                          :placeholder="editorOption.placeholder"
                          :ishljs="editorOption.ishljs" :codeStyle="editorOption.codeStyle"
                          :toolbarsFlag="editorOption.toolbarsFlag"
                          :subfield="editorOption.subfield" :defaultOpen="editorOption.defaultOpen"
                          :previewBackground="editorOption.previewBackground"
                          :autofocus="false"/>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>

import { articleInfoApi, listTagApi, selectedTagsApi } from '../../api/article'

export default {
  data() {
    return {
      zIndex: {
        'z-index': 1
      },
      editorOption: { //编辑器选项
        defaultValue: '# 默认值', //默认值
        fontSize: '14px', //编辑器字体
        placeholder: '开始编写内容吧...', //输入框为空时默认提示文本
        ishljs: true, //代码高亮
        codeStyle: 'atom-one-dark', //代码主题
        toolbarsFlag: true, //是否显示工具栏---用于显示的时候设置为false
        subfield: true, //true： 双栏(编辑预览同屏),false:单栏(编辑预览分屏)---用于显示的时候设置为false
        defaultOpen: '', //edit:默认展示编辑区域,preview： 默认展示预览区域 ---用于显示的时候设置为preview 不填的时候两边都有
        previewBackground: '#F1F3F4' //预览框背景颜色
      },
      form: {
        id: 0, //文章id
        title: '', //文章标题
        tagId: null, //标签分类
        options: [], //标签分类下拉框中的值
        articleType: null, //文章类型
        isCheckOks: false, //是否动过选择标签
        checkOks: [], //选择标签中的被选中的复选框
        tagList: [], //标签List
        describe: '', //文章描述
        cover: '', //封面图片
        markdown: '', //文章内容
        state: null, //文章状态
        uploadImgAction: '',//上传到的服务器地址
        isStickie: '0' // 是否置顶
      }
    }
  },
  created() {
    const id = this.$route.params.id
    if (typeof (id) != 'undefined') {
      this.form.id = id
      this.listTag()
      this.selectedTags()
      this.articleInfo(id)
      this.editorOption.subfield = false
      this.editorOption.defaultOpen = 'preview'
      this.editorOption.toolbarsFlag = false
    } else {
      this.$router.push('/article/list')
    }

  },
  methods: {
    /**
     * 标签列表
     */
    listTag() {
      listTagApi().then(res => {
        if (res.data.resultCode == 1) {
          this.form.options = res.data.resultData.allTags
          this.form.tagList = res.data.resultData.singleTags
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 已选择的标签
     */
    selectedTags() {
      selectedTagsApi(this.form.id).then(res => {
        if (res.data.resultCode == 1) {
          this.form.checkOks = res.data.resultData
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    articleInfo(id) {
      articleInfoApi(id).then(res => {
        if (res.data.resultCode == 1) {
          this.form.title = res.data.resultData.title
          this.form.articleType = res.data.resultData.articleType + ''
          this.form.state = res.data.resultData.state + ''
          this.form.tagId = res.data.resultData.tagId
          this.form.describe = res.data.resultData.articleDescribe
          this.form.cover = res.data.resultData.cover
          this.form.markdown = res.data.resultData.markdown
          this.form.isStickie = res.data.resultData.isStickie + ''
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     *单击返回
     */
    clickBack() {
      this.$router.push('/article/list')
    },
    /**
     * 单击导出
     */
    clickExport() {
      window.location.href = process.env.VUE_APP_BASE_API + '/article/exportMarkDown?id=' + this.form.id
    }
  }
}
</script>

<style lang="css" scoped>

.avatar-uploader .el-upload {
  border: 1px dashed #8c939d;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 250px;
  height: 158px;
  line-height: 158px;
  text-align: center;
  border: 1px dashed #8c939d;
  border-radius: 6px;
}

.avatar {
  width: 250px;
  height: 158px;
  display: block;
  border-radius: 10px;
}
</style>
