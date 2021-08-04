import { sendPostJSON, sendGet, sendGetParams, sendPostParams } from '@/utils/sendRequest'

export function treeApi() {
  return sendGet('/router/tree')
}

export function routerInfoApi(id) {
  return sendGetParams('/router/routerInfo', { id })
}

export function menuListApi() {
  return sendGet('/router/menuList')
}

export function updateSortApi(data) {
  return sendPostParams('/router/updateSort', data)
}

export function saveApi(type, data) {
  let url = ''
  switch (type) {
    case 1:
      url = '/router/addRouter'
      break
    case 2:
      url = '/router/editRouter'
      break
    case 3:
      url = '/router/deleteRouter'
      break
  }
  return sendPostJSON(url, data)
}

export function getRouterApi() {
  return sendGet('/router/getRouter')
}
