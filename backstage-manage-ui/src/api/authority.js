import { sendPostJSON, sendGet, sendGetParams } from '@/utils/sendRequest'

export function treeApi() {
  return sendGet('/authority/tree')
}

export function authorityInfoApi(id) {
  return sendGetParams('/authority/authorityInfo', { id: id })
}

export function saveApi(type, data) {
  let url = ''
  switch (type) {
    case 1:
      url = '/authority/addAuthority'
      break
    case 2:
      url = '/authority/editAuthority'
      break
    case 3:
      url = '/authority/delAuthority'
      break
  }
  return sendPostJSON(url, data)
}

export function apiListApi() {
  return sendGet('/authority/listApi')
}
