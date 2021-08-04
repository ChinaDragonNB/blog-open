import { sendPostParams, sendPostMultipart, sendPostJSON, sendGetParams } from '@/utils/sendRequest'

export function listTagApi(data) {
  return sendPostJSON('/tags/listTag', data)
}

export function uploadLogoApi(data) {
  return sendPostMultipart('/tags/uploadLogo', data)
}

export function tagInfoApi(id) {
  return sendGetParams('/tags/tagInfo', { id: id })
}

export function saveApi(action, data) {
  let url = ''
  switch (action) {
    case 'add':
      url = '/tags/addTag'
      break
    case 'edit':
      url = '/tags/editTag'
      break
  }
  return sendPostJSON(url, data)
}

export function delTagApi(id) {
  return sendPostParams( '/tags/delTag',{ tagId: id})
}
