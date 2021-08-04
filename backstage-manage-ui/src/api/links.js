import { sendPostJSON, sendPostParams, sendGetParams } from '@/utils/sendRequest'

export function listLinkApi(data) {
    return sendPostJSON('/links/listLink', data)
}

export function linkInfoApi(id) {
    return sendGetParams('/links/linkInfo', { id })
}

export function editLinkApi(data) {
    return sendPostJSON('/links/editLink', data)
}

export function delLinkApi(id) {
    return  sendPostParams('/links/delLink', { id})
}

export function checkLinkApi(data) {
    return sendPostParams('/links/checkLink', data)
}