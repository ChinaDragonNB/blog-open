<template>
  <div class="app-container">
    <el-form ref="form" :model="form" class="boxShadow">
      <el-row>
        <el-col :xl="10" :lg="10" :md="10" :sm="10" :xs="24" align="left">
          <el-form-item label="文章标题：">
            <el-input v-model="form.title" placeholder="请输入标题" clearable size="small" style="width:280px;"
                      autofocus></el-input>
          </el-form-item>
        </el-col>
        <el-col :xl="10" :lg="10" :md="10" :sm="10" :xs="24">
          <el-form-item label="所属分类：">
            <el-select v-model="form.tagId" placeholder="请选择所属分类" size="small" clearable>
              <el-option v-for="item in form.options" :key="item.value" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
            <!-- ---------- 选择标签嵌入框 start  ---------- -->
            <el-popover placement="top-end" trigger="click" width="550">
              <el-checkbox-group v-model="form.checkOks" size="mini" :max="3" @change="form.isCheckOks=true">
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
          <el-button size="small" type="primary" @click="commitData">保存</el-button>
          <el-button size="small" @click="clickBack">返回</el-button>
        </el-col>
      </el-row>
      <br/>
      <br/>
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
        <el-input type="textarea" :rows="6" placeholder="请输入文章描述" v-model="form.articleDescribe" clearable></el-input>
      </el-form-item>
      <el-form-item label="文章封面">
        <br>
        <el-input v-model="form.cover" placeholder="请输入封面链接" clearable size="small" style="width:50%;" autofocus/>
        <el-upload class="avatar-uploader" :action="form.uploadImgAction" :http-request="uploadCover"
                   :show-file-list="false" :before-upload="beforeAvatarUpload">
          <img v-if="form.cover" :src="form.cover" class="avatar" alt=""/>
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-row>
        <el-col :md="24" :xl="24" :lg="24" :xs="24">
          <br/>
          <br/>
          <span style="font-size:14px;font-weight:bold;color:#606266;">文章内容</span>
          <br/>
          <br/>
          <div class="mavonEditor">
            <mavon-editor ref="md" v-model="form.markdown" :style="zIndex" :toolbars="markdownOption"
                          :editorOption="editorOption" :value="form.markdown" :fontSize="editorOption.fontSize"
                          :placeholder="editorOption.placeholder" :ishljs="editorOption.ishljs"
                          :codeStyle="editorOption.codeStyle" :toolbarsFlag="editorOption.toolbarsFlag"
                          :subfield="editorOption.subfield" :defaultOpen="editorOption.defaultOpen"
                          :previewBackground="editorOption.previewBackground" :autofocus="false"
                          @save="save" @imgAdd="$imageAdd" @change="change" @fullScreen="fullScreen">
            </mavon-editor>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>

import { listTagApi, saveApi, uploadCoverApi, uploadImgApi } from '../../api/article'

export default {
  data() {
    return {
      zIndex: {
        'z-index': 1
      },
      editorOption: {
        //编辑器选项
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
      markdownOption: {
        //工具栏选项
        fullscreen: true, //全屏
        undo: true, //上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: true, // 保存（触发events中的save事件）
        code: true, //代码块
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        htmlcode: true, // 展示html源码
        preview: true, // 预览
        table: true // 表格
      },
      form: {
        id: 0, //文章id
        title: '', //文章标题
        tagId: null, //标签分类
        options: [], //标签分类下拉框中的值
        articleType: '1', //文章类型
        isCheckOks: false, //是否动过选择标签
        checkOks: [], //选择标签中的被选中的复选框
        tagList: [], //标签List
        articleDescribe: '', //文章描述
        cover: '', //封面图片
        markdown: '', //文章内容
        state: '1', //文章状态
        uploadImgAction: '',//上传到的服务器地址
        isStickie: '0', // 是否置顶
        isAddImage: false // 是否添加了图片
      }
    }
  },
  created() {
    /*  ---------------按下Ctrl+S-- start----------- */
    let _self = this
    document.onkeydown = function(e) {
      if (e.keyCode == 83 && (navigator.platform.match('Mac') ? e.metaKey : e.ctrlKey)) {
        e.preventDefault()
        _self.commitData()
      }
    }
    this.listTag()
  },
  methods: {
    /**
     *提交数据
     */
    commitData() {
      let action = 'write'
      if (this.form.id != 0) {
        action = 'edit'
      }
      const articleData = {
        isCheckOks: this.form.isCheckOks, //是否操作过选中的标签
        tagIds: this.form.checkOks, //新的选中的标签
        article: this.form,
        isAddImage: this.form.isAddImage
      }
      if (!articleData.article.title) {
        this.$message.error('请输入标题')
        return
      }
      if (!articleData.article.tagId) {
        this.$message.error('请选择所属分类')
        return
      }
      if (!articleData.article.tagList) {
        this.$message.error('请选择标签')
        return
      }
      if (!articleData.article.articleType) {
        this.$message.error('请选择文章类型')
        return
      }
      if (!articleData.article.state) {
        this.$message.error('请选择文章状态')
        return
      }
      if (!articleData.article.articleDescribe) {
        this.$message.error('请输入文章描述')
        return
      }
      if (!articleData.article.cover) {
        this.$message.error('请上传文章封面图片')
        return
      }
      if (!articleData.article.markdown) {
        this.$message.error('请输入文章内容')
        return
      }
      saveApi(action, articleData).then(res => {
          if (res.data.resultCode == 1) {
            this.$message.success(res.data.resultMessage)
            if (res.data.resultData != 'null' && res.data.resultData != null) {
              this.form.id = res.data.resultData
              this.form.isAddImage = false
            }
          } else if (res.data.resultCode == -1) {
            this.$message.error(res.data.resultMessage)
          }
        }
      )
    },
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
     * 图片上传之前的函数事件
     * @param file
     * @returns {boolean}
     */
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/webp' || file.type == 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传的封面图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传封面图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    /**
     * 上传封面回调函数
     * @param $file 文件信息
     */
    uploadCover($file) {
      const formData = new FormData()
      formData.append('image', $file.file)
      formData.append('uploadPrefix', 'cover')
      uploadCoverApi(formData).then(res => {
        if (res.data.resultCode == 1) {
          this.form.cover = res.data.resultData
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    clickBack() {
      this.$router.push('/article/list')
    },
    /**
     * 保存后的函数事件
     * @param value 新值
     * @param render 新的HTML内容
     */
    save(value, render) {
      this.form.markdown = value
      //保存之后将数据都存入数据库,将localStorage的值清掉
    },
    /**
     * 编辑区内容改变后的函数事件
     * @param value 新值
     * @param render 新的HTML内容
     */
    change(value, render) {
      this.form.markdown = value
    },
    /**
     * 点击全屏时的函数事件
     * @param status true：全屏
     * @param value
     */
    fullScreen(status, value) {
      if (status == 'true' || status == true) {
        this.zIndex = {
          'z-index': 1500
        }
      } else {
        this.zIndex = {
          'z-index': 1
        }
      }
    },
    /**
     * 添加图片时的回调函数事件
     * @param pos 图片名
     * @param $file 文件信息
     */
    $imageAdd(pos, $file) {
      // 绑定@imgAdd event
      const formdata = new FormData()
      formdata.append('image', $file)
      formdata.append('uploadPrefix', 'markdown')
      uploadImgApi(formdata).then(res => {
        if (res.data.resultCode == 1) {
          // * 例如：返回数据为 y
          this.$refs.md.$img2Url(pos, res.data.resultData)
          this.form.isAddImage = true
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    }
  }
}
</script>

<style scoped>
.mavon-editor-toolbar {
  box-sizing: border-box;
  display: inline-block;
  cursor: pointer;
  height: 28px;
  width: 28px;
  margin: 6px 0 5px 0px;
  font-size: 15px;
  padding: 4.5px 6px 5px 3.5px;
  color: #757575;
  border-radius: 5px;
  text-align: center;
  background: none;
  border: none;
  outline: none;
  line-height: 1;
}

.mavon-editor-toolbar:hover {
  background-color: #E9E9EB;
  color: #000000;
}


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
  width: 200px;
  height: 158px;
  display: block;
  border-radius: 10px;
}
</style>
