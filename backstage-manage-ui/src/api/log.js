import { sendGetParams } from '@/utils/sendRequest'

export function articleLogsApi(data) {
    return sendGetParams('/log/articleLogs', data)
}

export function loginLogsApi(data) {
    return sendGetParams('/log/loginLogs', data)
}

export function operLogsApi(data) {
    return sendGetParams('/log/operLogs', data)
}


