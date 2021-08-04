<template>
    <div class="app-container">
        <el-row>
            <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
                <el-select v-model="searchArticleId" placeholder="请选择文章" size="small">
                    <el-option v-for="item in articleList" :key="item.value"
                               :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
                <el-date-picker v-model="commentTime" type="daterange" size="small" range-separator="-"
                                start-placeholder="评论时间"
                                end-placeholder="评论时间" align="center">
                </el-date-picker>
                <el-button @click="clickSearch" icon="el-icon-search" size="small">查询</el-button>
            </el-col>
        </el-row>
        <br/>
        <el-table v-loading="listLoading" :data="list"
                  element-loading-text="加载中..." highlight-current-row>
            <el-table-column type="index" align="center" label="序号"></el-table-column>
            <el-table-column label="评论文章" align="center">
                <template slot-scope="scope">
                    <span style>{{ scope.row.articleTitle }}</span>
                </template>
            </el-table-column>
            <el-table-column label="评论者" align="center">
                <template slot-scope="scope">
                    <span style>{{ scope.row.userName }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="评论时间">
                <template slot-scope="scope">
                    <i class="el-icon-time"/>
                    <span>{{ scope.row.commentTime }}</span>
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.state==1" type="warning" effect="dark">未审核</el-tag>
                    <el-tag v-if="scope.row.state==2" type="success" effect="dark">已通过</el-tag>
                    <el-tag v-if="scope.row.state==3" type="danger" effect="dark">未通过</el-tag>
                    <el-tag v-if="scope.row.state==4" type="info" effect="dark">已删除</el-tag>
                </template>
            </el-table-column>
            <el-table-column class-name="status-col" label="操作" align="center">
                <template slot-scope="scope">
                    <el-button size="mini" v-if="scope.row.state==1" @click="checkComment(2, scope.row.id)"
                               type="success">
                        通过
                    </el-button>
                    <el-button size="mini" v-if="scope.row.state==1" @click="checkComment(3, scope.row.id)"
                               type="warning">
                        不通过
                    </el-button>
                    <el-button size="mini" @click="clickView(scope.$index, scope.row)">查看</el-button>
                    <el-button type="danger" size="mini" @click="clickDel(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <br/>
        <el-pagination background layout="prev, pager, next" :total="total" :current-page="pageNum"
                       :page-size="pageSize"
                       :page-count="pages" :pager-count="5" @next-click="nextClick" @current-change="currentChange"
                       @prev-click="prevClick"></el-pagination>
        <el-dialog title="查看评论" :visible.sync="dialogVisible">
            <el-form ref="form" :model="comment" label-width="80px">
                <el-form-item label="评论文章">
                    <el-input v-model="comment.articleTitle" size="small" readonly></el-input>
                </el-form-item>
                <el-form-item label="评论者">
                    <el-input v-model="comment.userName" size="small" readonly></el-input>
                </el-form-item>
                <el-form-item label="评论内容">
                    <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 10}" v-model="comment.commentConetnt"
                              size="small"
                              readonly></el-input>
                </el-form-item>
                <el-form-item label="评论时间">
                    <el-date-picker v-model="comment.commentTime" type="datetime" align="right" size="small"
                                    readonly></el-date-picker>
                </el-form-item>
                <el-form-item label="评论者IP">
                    <el-input v-model="comment.commentIp" size="small" readonly></el-input>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import {
    sendPostParams
} from '@/utils/sendRequest'
import { sendPost } from '../../utils/sendRequest'

export default {
    data() {
        return {
            list: null, // 数据集合
            listLoading: true, // 加载
            pageNum: 1, // 当前页
            pageSize: 10, // 每页显示多少条数据
            total: 0, // 总条数
            prePage: 0, // 上一页
            nextPage: 0, // 下一页
            pages: 0,// 总页数
            dialogVisible: false,// 窗口
            comment: { //评论对象
                id: '',// 主键
                articleId: '',// 评论的文章
                articleTitle: '',
                userName: '',// 评论人名称
                imgUrl: '',// 头像链接
                commentConetnt: '',// 评论内容
                commentTime: '', // 评论时间
                commentIp: ''  // 评论人ip
            },
            articleList: [], // 文章信息集合
            searchArticleId: null, //下拉框选择的文章ID
            commentTime: '', // 评论时间下拉框赋值
            timeStart: '', // 评论时间段
            timeEnd: ''// 评论时间段
        }
    },
    created() {
        this.fetchData(this.pageNum, this.pageSize)
        this.findArticleList()
    },
    methods: {
        /**
         * 文章下拉框
         */
        findArticleList() {
            sendPost('/comment/findArticleList').then(res => {
                if (res.data.resultCode == 1) {
                    this.articleList = res.data.resultData
                } else if (res.data.resultCode == -1) {
                    this.$message.error(res.data.resultMessage)
                }
            })
        },
        /**
         *单击查询按钮
         */
        clickSearch() {

            if (this.commentTime != '' && this.commentTime != null) {
                // 时间格式化
                this.timeStart = this.dateFormat('YYYY-mm-dd', this.commentTime[0])
                this.timeEnd = this.dateFormat('YYYY-mm-dd', this.commentTime[1])
            } else {
                this.timeStart = ''
                this.timeEnd = ''
            }
            this.fetchData(this.pageNum, this.pageSize)
        },
        /**
         * 排序方式
         */
        sortChange(data) {
        },
        /**
         * 获取数据
         * @param pageNum 当前页
         * @param pageSize 每页的显示的数量
         */
        fetchData(pageNum, pageSize) {
            this.listLoading = true
            var params = {
                pageNum,
                pageSize,
                articleId: this.searchArticleId,
                timeStart: this.timeStart,
                timeEnd: this.timeEnd
            }
            sendPostParams('/comment/findList', params).then(res => {
                if (res.data.resultCode == 1) {
                    this.list = res.data.resultData.list
                    this.total = parseInt(res.data.resultData.total)
                    this.pageNum = parseInt(res.data.resultData.pageNum)
                    this.pageSize = parseInt(res.data.resultData.pageSize)
                    this.prePage = parseInt(res.data.resultData.prePage)
                    this.nextPage = parseInt(res.data.resultData.nextPage)
                    this.pages = parseInt(res.data.resultData.pages)
                } else if (res.data.resultCode == -1) {
                    this.$message.error(res.data.resultMessage)
                }
                this.listLoading = false
            })
        },
        /**
         * 上一页
         */
        nextClick() {
            this.fetchData(this.nextPage, this.pageSize)
        },
        /**
         * 下一页
         */
        prevClick() {
            this.fetchData(this.prePage, this.pageSize)
        },
        /**
         * 当页数改变时
         * @param pageNum 当前页
         */
        currentChange(pageNum) {
            this.fetchData(pageNum, this.pageSize)
        },
        /**
         * 单击查看
         */
        clickView(index, row) {
            var id = row.id
            var param = {
                id
            }
            sendPostParams('/comment/findById', param).then(res => {
                if (res.data.resultCode == 1) {
                    this.comment = res.data.resultData
                    this.dialogVisible = true
                } else {
                    this.$message.error(res.data.resultMessage)
                }
            })
        },
        /**
         * 单击删除时
         * @param index
         * @param row
         */
        clickDel(index, row) {
            this.$confirm('确定删除该评论吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                var id = row.id
                var params = {
                    id
                }
                sendPostParams('/comment/deleteComment', params).then(res => {
                    if (res.data.resultCode == 1) {
                        this.$message.success(res.data.resultMessage)
                        this.list.splice(index, 1)
                    } else if (res.data.resultCode == -1) {
                        this.$message.error(res.data.resultMessage)
                    }
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                })
            })
        },
        /**
         * 审核评论
         */
        checkComment(state, id) {
            var param = {
                state, id
            }
            sendPostParams('/comment/checkComment', param).then(res => {
                if (res.data.resultCode == 1) {
                    this.$message.success(res.data.resultMessage)
                    this.fetchData(this.pageNum, this.pageSize)
                } else {
                    this.$message.error(res.data.resultMessage)
                }
            })
        },
        /**
         * 时间格式化
         * @param fmt 格式化
         * @param date 时间
         * @returns {*}
         */
        dateFormat(fmt, date) {
            let ret
            let opt = {
                'Y+': date.getFullYear().toString(),        // 年
                'm+': (date.getMonth() + 1).toString(),     // 月
                'd+': date.getDate().toString(),            // 日
                'H+': date.getHours().toString(),           // 时
                'M+': date.getMinutes().toString(),         // 分
                'S+': date.getSeconds().toString()          // 秒
                // 有其他格式化字符需求可以继续添加，必须转化成字符串
            }
            for (let k in opt) {
                ret = new RegExp('(' + k + ')').exec(fmt)
                if (ret) {
                    fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, '0')))
                }

            }
            return fmt
        }
    }
}
</script>


<style>
</style>
