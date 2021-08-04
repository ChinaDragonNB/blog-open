import { sendGet } from '@/utils/sendRequest'

export function getLatelyArticleApi() {
    return sendGet('/main/getLatelyArticle')
}

export function getWebsiteToolsApi() {
    return sendGet('/main/getWebsiteTools')
}

export function statisticsApi() {
    return sendGet('/main/getStatistics')
}

export function articlePieApi() {
    return sendGet('/main/articlePie')
}

export function tagColumnApi() {
    return sendGet('/main/tagColumn')
}

export function linkLineApi() {
    return sendGet('/main/linkLine')
}

export function browseChartsApi() {
    return sendGet('/main/browseCharts')
}



