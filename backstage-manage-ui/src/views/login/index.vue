<template>
  <div id="login">
    <el-form ref="loginForm" :model="loginForm" :rules="rules" label-position="left" class="login-box"
             autocomplete="off">
      <div class="title-container">
        <h3 class="title">博客后台管理</h3>
      </div>
      <el-form-item prop="userName">
        <el-input type="text" placeholder="用户名" v-model="loginForm.userName" autofocus clearable prefix-icon="el-icon-user" @keyup.enter.native="onSubmit('loginForm')"/>
      </el-form-item>
      <el-form-item prop="userPass">
        <el-input type="password" placeholder="密码" v-model="loginForm.userPass" clearable prefix-icon="el-icon-key" @keyup.enter.native="onSubmit('loginForm')"/>
      </el-form-item>
      <el-form-item class="code-input" prop="code">
        <el-input placeholder="验证码" autocomplete="off" name="code" prefix-icon="el-icon-lock" ref="code" type="text" v-model="loginForm.code" clearable @keyup.enter.native="onSubmit('loginForm')"/>
      </el-form-item>
      <img :src="imageCode" @click="getCodeImage" alt="codeImage" class="code-image"/>
      <el-form-item class="remember">
        <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
      </el-form-item>
      <el-button type="primary" :loading="loading" @click="onSubmit('loginForm')"
                 style="width:100%;margin-bottom:14px;">登录
      </el-button>
      <span>
        <el-divider>第三方登录</el-divider>
        <!--        <div class="thirdparty"></div>-->
        <div style="margin-top: -20px">
          <template v-for="(l, index) in logo">
            <div :key="index" class="logo-wrapper">
              <img class="logImg" :style="l.style"
                   :src="resolveLogo(l.img)"
                   @click="socialLogin(l.name)"
                   alt
              />
            </div>
          </template>
        </div>
      </span>
    </el-form>
  </div>
</template>

<script>
import { loginApi, captchaApi } from '@/api/auth'

// Token工具
import { setToken, removeToken } from '@/utils/auth'
import { randomNum } from '@/utils'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        userName: process.env.VUE_APP_USER_NAME,
        userPass: process.env.VUE_APP_USER_PASS,
        code: '',
        remember: null
      },
      codeKey: '',
      imageCode: '',
      // 是否登录中
      loading: false,
      logo: [
        { img: 'qq.png', name: 'qq', style: '' },
        { img: 'github.png', name: 'github', style: 'border-radius: 50%;' },
        { img: 'weibo.png', name: 'weibo', style: '' },
        { img: 'gitee.png', name: 'gitee', style: 'border-radius: 50%;' },
        { img: 'dingtalk.png', name: 'dingtalk', style: 'border-radius: 50%;' }
      ],
      // 表单验证，需要在 el-tags-item 元素中增加 prop 属性
      rules: {
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        userPass: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' }
        ]
      }
    }
  }, mounted() {
    this.getCodeImage()
  },
  methods: {
    onSubmit(formName) {
      if (this.loading) {
        this.$message.info('登录中...')
        return false
      }
      let _self = this
      // 为表单绑定验证功能
      this.$refs[formName].validate((valid) => {
        if (valid) {
          _self.loading = true
          const param = this.loginForm
          param.codeKey = this.codeKey
          loginApi(param).then(res => {
            if (res.data.resultCode == 1) {
              this.loginSuccess(res.data)
            } else {
              this.loginError(res.data)
              this.loginForm.code = ''
              this.getCodeImage()
            }
          })
        } else {
          return false
        }
      })
    },
    getCodeImage() {
      this.codeKey = randomNum(24, 16)
      captchaApi(this.codeKey).then(res => {
        if (res.data.byteLength <= 100) {
          this.$message.error('获取验证码失败')
        }
        this.imageCode = ('data:image/png;base64,' + btoa(new Uint8Array(res.data).reduce((data, byte) => data + String.fromCharCode(byte), '')))
      })

    },
    loginSuccess(data) {
      this.$message.success(data.resultMessage)
      removeToken()
      // 设置Token
      setToken(data.resultData)
      this.$router.push('/main')
    },
    loginError(data) {
      this.$message.error(data.resultMessage)
      this.loading = false
    },
    resolveLogo(logo) {
      return require(`@/assets/logo/${logo}`)
    }
  }
}
</script>

<style>

#login {
  background: url("https://cdn.ak47007.com/images/loginbackground.jpg?x-oss-process=style/1") 50% no-repeat;
  width: 100%;
  height: 100vh;
  background-size: cover;
}

#login .login-box {
  position: absolute;
  top: 50%;
  left: 70%;
  margin: -220px 0 0 -120px;
  width: 320px;
  height: 480px;
  padding: 36px;
  background: #fff;
  border-radius: 3px;
}

#login .title-container {
  position: relative;
}

#login .title {
  font-size: 20px;
  color: rgba(0, 0, 0, 0.85);
  margin: 0 auto 40px auto;
  text-align: center;
  font-weight: bold;
}

#login .code-input {
  width: 50%;
  display: inline-block;
  vertical-align: middle;
}

#login .remember .el-form-item__content {
  line-height: 0px;
}

#login .code-image {
  display: inline-block;
  vertical-align: top;
  cursor: pointer;
  height: 42px;
  width: 115px;
}

#login .logo-wrapper {
  display: inline-block;
  margin: 10px 0;
}

#login .logImg {
  width: 1.5rem;
  display: inline-block;
  margin: 0.8rem 0.8rem -0.8rem 0.8rem;
  cursor: pointer;
}

#login .thirdparty {
  text-align: center;
  font-size: 14px;
  color: #3E3E3E;
}

@media screen and (max-width: 1100px) {
}

@media screen and (max-width: 970px) {
  #login .login-box {
    left: 50%;
  }
}


</style>
