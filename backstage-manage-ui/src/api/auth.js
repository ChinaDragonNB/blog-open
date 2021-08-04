import { sendGet, sendPost, sendGetImg, sendPostParams } from '@/utils/sendRequest'

export function getUserInfoApi() {
    return sendGet('/auth/getUserInfo')
}

export function logoutApi() {
    return sendPost('/auth/logout')
}

export function loginApi(param) {
    return sendPostParams('/auth/login', param)
}

export function captchaApi(key) {
    return sendGetImg('/auth/captcha', { key })
}


