<template>
  <div class="mavonEditor">
    <mavon-editor ref=md
                  v-model="markDownValue"
                  :style="zIndex" :toolbars="markdownOption" :editorOption="editorOption"
                  :value="editorOption.defaultValue" :fontSize="editorOption.fontSize"
                  :placeholder="editorOption.placeholder" :ishljs="editorOption.ishljs"
                  :codeStyle="editorOption.codeStyle" :toolbarsFlag="editorOption.toolbarsFlag"
                  :subfield="editorOption.subfield" :defaultOpen="editorOption.defaultOpen"
                  :previewBackground="editorOption.previewBackground" :autofocus="false"
                  @save="save" @imgAdd="$imageAdd" @change="change" @fullScreen="fullScreen"
    />

  </div>
</template>
<script>
    import {sendPostMultipart} from '@/utils/sendRequest.js'

    export default {
        data() {
            return {
                zIndex: {
                    "z-index": 1
                },
                markDownValue: "",
                editorOption: {  //编辑器选项
                    defaultValue: "# 默认值",  //默认值
                    fontSize: "16px",  //编辑器字体
                    placeholder: "开始编写内容吧...", //输入框为空时默认提示文本
                    ishljs: true, //代码高亮
                    codeStyle: "atom-one-dark", //代码主题
                    toolbarsFlag: true, //是否显示工具栏---用于显示的时候设置为false
                    subfield: true, //true： 双栏(编辑预览同屏),false:单栏(编辑预览分屏)---用于显示的时候设置为false
                    defaultOpen: "", //edit:默认展示编辑区域,preview： 默认展示预览区域 ---用于显示的时候设置为preview 不填的时候两边都有
                    previewBackground: "#F1F3F4" //预览框背景颜色
                },
                markdownOption: { //工具栏选项
                    fullscreen: true, //全屏
                    undo: true,//上一步
                    redo: true, // 下一步
                    trash: true, // 清空
                    save: true, // 保存（触发events中的save事件）
                    code: true, //代码块
                    quote: true, // 引用
                    ol: true, // 有序列表
                    ul: true, // 无序列表
                    link: true, // 链接
                    imagelink: true, // 图片链接
                    htmlcode: true, // 展示html源码
                    preview: true, // 预览
                    table: true // 表格

                }
            }
        }, created() {
            var data = this.giveChildData();
            console.log(data)
            if (typeof (data) != "undefined") {
                this.editorOption.defaultValue = data.markdown;
                if (data.palce == 'articleView') {
                    this.editorOption.defaultOpen = 'preview';
                }
            }

            console.log(form);
        },
        methods: {
            /**
             * 保存后的函数事件
             * @param value 新值
             * @param render 新的HTML内容
             */
            save(value, render) {
                this.editorOption.markDownValue = value;
                //保存之后将数据都存入数据库,将localStorage的值清掉
                this.$emit('commitData');
            },
            /**
             * 编辑区内容改变后的函数事件
             * @param value 新值
             * @param render 新的HTML内容
             */
            change(value, render) {
                this.editorOption.markDownValue = value;
            },
            /**
             * 点击全屏时的函数事件
             * @param status true：全屏
             * @param value
             */
            fullScreen(status, value) {
                if (status == "true" || status == true) {
                    this.zIndex = {
                        "z-index": 1500
                    };
                } else {
                    this.zIndex = {
                        "z-index": 1
                    };
                }
            },
            /**
             * 添加图片时的回调函数事件
             * @param pos 图片名
             * @param $file 文件信息
             */
            $imageAdd(pos, $file) { // 绑定@imgAdd event
                // 缓存图片信息
                this.uploadImage(pos, $file)
            },
            /**
             * 上传图片的方法
             * @param pos
             * @param $file
             */
            uploadImage(pos, $file) {
                var formdata = new FormData();
                formdata.append("image", $file);
                formdata.append("uploadPlace", "markDownImage");
                sendPostMultipart('/article/uploadImage', formdata).then((response) => {
                    console.log(response)
                    // * 例如：返回数据为 y
                    this.$refs.md.$img2Url(pos, response.data.resultData.url);
                })
            }, getValue() {
                return this.markDownValue;
            }
        }
    }
</script>
<style>
  .mavonEditor {
    width: 100%;
    height: 100%;
  }

</style>
