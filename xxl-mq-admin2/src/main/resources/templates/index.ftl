<!DOCTYPE html>
<html>
<head>
    <#-- import macro -->
  	<#import "./common/common.macro.ftl" as netCommon>
    <#-- commonStyle -->
	<@netCommon.commonStyle />

    <#-- biz start（1/5 style） -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
    <#-- biz end（1/5 end） -->

</head>
<body class="hold-transition skin-blue sidebar-mini" >
<div class="wrapper" >

    <!-- header -->
    <@netCommon.commonHeader />

    <!-- left -->
    <#-- biz start（2/5 left） -->
    <@netCommon.commonLeft "/index" />
    <#-- biz end（2/5 left） -->

    <!-- right start -->
    <div class="content-wrapper">

        <!-- content-header -->
        <section class="content-header">
            <#-- biz start（3/5 name） -->
            <h1>${I18n.index_name}</h1>
            <#-- biz end（3/5 name） -->
        </section>

        <!-- content-main -->
        <section class="content">

            <#-- biz start（4/5 content） -->

            <!-- 报表摘要 start -->
            <div class="row">
                <div class="col-md-4 col-sm-6 col-xs-12">
                    <div class="info-box">
                        <span class="info-box-icon bg-aqua"><i class="fa fa-cloud"></i></span>
                        <div class="info-box-content">
                            <span class="info-box-text">服务数量</span>
                            <span class="info-box-number">***</span>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 col-sm-6 col-xs-12">
                    <div class="info-box">
                        <span class="info-box-icon bg-red"><i class="fa fa-cubes"></i></span>
                        <div class="info-box-content">
                            <span class="info-box-text">主题数量</span>
                            <span class="info-box-number">***</span>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 col-sm-6 col-xs-12">
                    <div class="info-box">
                        <span class="info-box-icon bg-green"><i class="ion ion-ios-gear-outline"></i></span>
                        <div class="info-box-content">
                            <span class="info-box-text">消息数量</span>
                            <span class="info-box-number">***</span>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 报表摘要 end --->

            <!-- 常用功能区域 start -->
            <div class="row">
                <div class="col-md-12">
                    <!-- 常用功能 -->
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">常用功能</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">

                            <strong><i class="fa fa-cubes margin-r-5"></i>消息管理</strong>
                            <p class="text-muted">
                                提供消息查看及管理相关能力，支持线上化检索查看消息数据、消费轨迹等，以及支持新增和变更消息数据等。
                            </p>

                            <hr>
                            <strong><i class="fa fa-cloud margin-r-5"></i>主题管理</strong>
                            <p class="text-muted">
                                提供消息主题定义、管理能力，支持人工维护消息主题数据，默认消息主题自动上报注册等。
                            </p>

                            <hr>
                            <strong><i class="fa fa-book margin-r-5"></i> 帮助中心</strong>
                            <p>提供内容丰富、干练易懂的操作文档，辅助快速上手项目。</p>

                        </div>
                        <!-- /.box-body -->
                    </div>
                </div>

            </div>
            <!-- 个人信息区域 end -->

            <!-- 查看通知.模态框 start -->
            <div class="modal fade" id="showMessageModal" tabindex="-1" role="dialog"  aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title" >查看通知</h4>
                        </div>
                        <div class="modal-body">
                            <form class="form-horizontal form" role="form" >
                                <div class="form-group">
                                    <label for="lastname" class="col-sm-2 control-label2">标题</label>
                                    <div class="col-sm-8 title" ></div>
                                </div>
                                <div class="form-group">
                                    <label for="lastname" class="col-sm-2 control-label2">操作时间</label>
                                    <div class="col-sm-8 addTime" ></div>
                                </div>
                                <div class="form-group">
                                    <label for="lastname" class="col-sm-2 control-label2">正文</label>
                                    <div class="col-sm-8 content" style="overflow: hidden;" ></div>
                                </div>

                                <div class="form-group" style="text-align:center;border-top: 1px solid #e4e4e4;">
                                    <div style="margin-top: 10px;" >
                                        <button type="button" class="btn btn-primary" data-dismiss="modal" >关闭</button>
                                    </div>
                                </div>

                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 查看通知.模态框 end -->

            <#-- biz end（4/5 content） -->

        </section>

    </div>
    <!-- right end -->

    <!-- footer -->
    <@netCommon.commonFooter />
</div>

<#-- commonScript -->
<@netCommon.commonScript />

<#-- biz start（5/5 script） -->
<!-- daterangepicker -->
<script src="${request.contextPath}/static/adminlte/bower_components/moment/moment.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
<!-- echarts -->
<script src="${request.contextPath}/static/plugins/echarts/echarts.common.min.js"></script>
<!-- js file -->
<script src="${request.contextPath}/static/js/index.js"></script>
<#-- biz end（5/5 script） -->

</body>
</html>
