import { sendGet, sendGetParams, sendPostMultipart, sendPostParams } from '@/utils/sendRequest'

export function getBucketListApi() {
    return sendGet('/aliCloud/getBucketList')
}

export function getObjectListApi(data) {
    return sendGetParams('/aliCloud/getObjectList', data)
}

export function addFolderApi(data) {
    return sendPostParams('/aliCloud/addFolder', data)
}

export function deleteObjectApi(data) {
    return sendPostParams('/aliCloud/deleteObject', data)
}

export function uploadFileApi(data) {
    return sendPostMultipart('/aliCloud/uploadFile', data)
}