<template>
  <div class="box" style="padding: 20px;">
    <el-form ref="form" :model="form" label-width="100px">
      <el-form-item label="标签名">
        <el-input size="small" v-model="form.tagName" autofocus placeholder="请输入标签名" required clearable style="width: 400px;"></el-input>
      </el-form-item>
      <el-form-item label="标签类型">
        <el-radio v-model="form.tagType" label="1">单个</el-radio>
        <el-radio v-model="form.tagType" label="2">组合</el-radio>
      </el-form-item>
      <el-form-item label="标签LOGO">
        <el-upload :action="form.uploadImgAction" class="avatar-uploader" :show-file-list="false" :before-upload="beforeAvatarUpload" :http-request="uploadImage">
          <img v-if="form.tagLogo" :src="form.tagLogo" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" @click="onSubmit">添加</el-button>
        <el-button size="small" @click="$router.push('/tags/list');"> 返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>


import { saveApi, uploadLogoApi } from '../../api/tag'

export default {
  data() {
    return {
      form: {
        tagName: '',
        tagType: '1',
        tagLogo: '', //默认图片路径
        uploadImgAction: '' //上传到的服务器地址
      }
    }
  },
  methods: {
    /**
     *提交
     */
    onSubmit() {
      if (this.form.tagName == '') {
        this.$message.error('请输入标签名称')
        return false
      }
      if (this.form.tagType == '') {
        this.$message.error('请选择标签类型')
        return false
      }
      if (this.form.tagLogo == '') {
        this.$message.error('请上传标签LOGO')
        return false
      }
      const params = {
        tagName: this.form.tagName,
        tagType: this.form.tagType,
        tagLogo: this.form.tagLogo
      }
      saveApi('add', params).then(res => {
        if (res.data.resultCode == 1) {
          this.$message.success(res.data.resultMessage)
          this.clearAll()
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    },
    /**
     * 上传图片之前的回调函数事件
     * @param file 文件
     * @returns {boolean}
     */
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/svg+xml'
      const isLt2M = file.size / 1024 / 1024 < 5

      if (!isJPG) {
        this.$message.error('上传标签logo只能是 svg 格式')
      }
      if (!isLt2M) {
        this.$message.error('上传标签Logo大小不能超过 5MB')
      }
      return isJPG && isLt2M
    },
    /**
     * 自定义覆盖上传函数
     * @param $file 文件信息
     */
    uploadImage($file) {
      const formData = new FormData()
      formData.append('image', $file.file)
      formData.append('uploadPrefix', 'tag')
      uploadLogoApi(formData).then(res => {
        if (res.data.resultCode == 1) {
          this.form.tagLogo = res.data.resultData
        } else if (res.data.resultCode == -1) {
          this.$message.error(res.data.resultMessage)
        }
      })
    }, clearAll() {
      this.form.tagLogo = ''
      this.form.tagType = 1
      this.form.tagName = ''
    }
  }
}
</script>
<style lang="css" scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
