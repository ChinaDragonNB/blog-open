import { sendPostJSON, sendGet, sendGetParams, sendPostMultipart } from '@/utils/sendRequest'

export function listUserApi(params) {
  return sendPostJSON('/user/list', params)
}

export function roleListApi() {
  return sendGet('/user/roleList')
}

export function userInfoApi(id) {
  return sendGetParams('/user/userInfo', { id })
}

export function delUserApi(id) {
  return sendPostJSON('/user/delUser', id)
}

export function uploadImageApi(data) {
  return sendPostMultipart('/user/uploadAvatar', data)
}

export function saveApi(type, data) {
  let url = '/user/addUser'
  if (type === 2) {
    url = '/user/editUser'
  }
  return sendPostJSON(url, data)
}
