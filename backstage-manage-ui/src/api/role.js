import { sendPostJSON, sendGet, sendGetParams } from '@/utils/sendRequest'

export function listRoleApi(data) {
  return sendPostJSON('/role/list', data)
}

export function roleInfoApi(id) {
  return sendGetParams('/role/roleInfo', { id })
}

export function delRoleApi(id) {
  return sendPostJSON('/role/delRole', id)
}

export function authorityTreeApi() {
  return sendGet('/role/authorityTree')
}

export function routerTreeApi() {
  return sendGet('/role/routerTree')
}

export function saveApi(type, data) {
  let url = ''
  if (type == 1) {
    url = '/role/addRole'
  } else if (type == 2) {
    url = '/role/editRole'
  }
  return sendPostJSON(url, data)
}


