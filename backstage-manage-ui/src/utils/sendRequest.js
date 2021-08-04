import axios from 'axios'
// 使用qs将axios发送的数据格式转换为form-data格式（在安装axios时，默认就安装了）
import qs from 'qs'
import router from '../router'

// Token工具
import { removeToken } from '@/utils/auth'

//每个请求都会加上这个url前缀
axios.defaults.baseURL = process.env.VUE_APP_BASE_API

// 每个请求都需要这个请求头
axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest'
axios.defaults.withCredentials = true

/**
 * 发送GET请求,不带参数
 * @param url 请求URL
 */
export function sendGet(url) {
    return axios({
        url,
        method: 'GET'
    })
}

/**
 * 发送GET请求,带有参数
 * @returns {AxiosPromise}
 */
export function sendGetParams(url, params) {
    return axios({
        url: url,
        method: 'GET',
        params: params
    })
}

/**
 * 发送Post请求,没有参数
 * @param url 接口路径
 * @returns {AxiosPromise}
 */
export function sendPost(url) {
    return axios({
        url,
        method: 'POST'
    })
}

/**
 *
 * @param url 接口路径
 * @param params 参数
 * @returns {AxiosPromise}
 */
export function sendPostParams(url, params) {
    return axios({
        url,
        method: 'POST',
        params: params
    })
}

/**
 * 发送POST请求，参数为JSON
 * @param url 接口路径
 * @param data json格式数据
 */
export function sendPostJSON(url, data) {
    return axios({
        url,
        method: 'POST',
        data: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

/**
 * 上传文件的Post请求方法
 * @param url 接口路径
 * @param data 数据
 */
export function sendPostMultipart(url, data) {
    return axios({
        url,
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

/**
 * 模拟post表单提及
 * @param url 路径
 * @param param 参数
 */
export function sendPostForm(url, param) {
    // 将请求数据转换为form-data格式
    param = qs.stringify(param)
    return axios({
        url,
        method: 'post',
        data: param,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
}

/**
 * 发送get请求获取图片二进制流
 * @param url 路径
 */
export function sendGetImg(url, param) {
    // 将请求数据转换为form-data格式
    return axios({
        url,
        method: 'get',
        params: param,
        responseType: 'arraybuffer'
    })
}

//是否能发送请求
let isRequest = true

// http request 拦截器
axios.interceptors.request.use(
  config => {
      if (config.url == '/auth/login' || config.url == '/auth/captcha') {
          isRequest = true
      }
      if (isRequest) {
          return config
      }
  },
  err => {
      return Promise.reject(err)
  })

// http response 拦截器
axios.interceptors.response.use(
  res => {
      if (res.data.resultCode == 401) {
          if (isRequest) {
              toLogin()
          }
      }
      return res
  }, error => {   //接口错误状态处理，也就是说无响应时的处理
      if (!error.response) {
          toLogin()
      } else {
          return Promise.reject(error.response.status) // 返回接口返回的错误信息
      }
  })

/**
 * 跳转到登录
 */
function toLogin() {
    isRequest = false
    removeToken()
    console.log('跳转到登陆')
    router.push('/login')
}


