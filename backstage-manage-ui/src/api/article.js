import { sendPostJSON, sendGet, sendGetParams, sendPostParams, sendPostMultipart } from '@/utils/sendRequest'

export function listArticleApi(data) {
  return sendPostJSON('/article/listArticle', data)
}

export function delArticleApi(id) {
  return sendPostParams('/article/delArticle', { id })
}

export function uploadMarkDownApi(data) {
  return sendPostMultipart('/article/uploadMarkDown', data)
}

export function replaceArticleContentApi(articleId) {
  return sendPostParams('/article/replaceArticleContent', { articleId })
}

export function saveApi(action, data) {
  let url = ''
  switch (action) {
    case 'write':
      url = '/article/writeArticle'
      break
    case 'edit':
      url = '/article/editArticle'
      break
  }
  return sendPostJSON(url, data)
}

export function listTagApi() {
  return sendGet('/article/listTag')
}

export function selectedTagsApi(id) {
  return sendGetParams('/article/selectedTags', { id })
}

export function articleInfoApi(id) {
  return sendGetParams('/article/articleInfo', { id })
}

export function uploadCoverApi(data) {
  return sendPostMultipart('/article/uploadCover', data)
}

export function uploadImgApi(data) {
  return sendPostMultipart('/article/uploadImg', data)
}
