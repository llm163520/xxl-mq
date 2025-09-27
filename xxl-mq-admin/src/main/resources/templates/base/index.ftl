<!DOCTYPE html>
<html>
<head>
    <#-- import macro -->
    <#import "../common/common.macro.ftl" as netCommon>

    <!-- 1-style start -->
    <@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/biz/common/admin.tab.css">
    <!-- 1-style end -->

</head>
<body class="hold-transition skin-blue sidebar-mini fixed" >
<div class="wrapper" >

    <!-- 2-header start -->
    <header class="main-header">
        <!-- header logo -->
        <a href="${request.contextPath}/" class="logo">
            <span class="logo-mini"><b>XXL</b></span>
            <span class="logo-lg"><b>${I18n.admin_name}</b></span>
        </a>
        <nav class="navbar navbar-static-top" role="navigation">
            <!--header left -->
            <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <!--header right -->
            <div class="navbar-custom-menu">
                <ul class="nav navbar-nav">

                    <#-- login user -->
                    <li class="dropdown">
                        <a href="javascript:" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false" style="font-weight: bold;">
                            ${I18n.system_welcome}：${xxl_sso_user.userName!}
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu" role="menu">
                            <li id="updatePwd" >
                                <a href="javascript:" style="height: 30px;padding: 3px 25px;" ><i class="fa fa-key"></i> ${I18n.change_pwd}</a>
                            </li>
                            <li id="changeSkin" >
                                <a href="javascript:" style="height: 30px;padding: 3px 25px;" ><i class="fa fa-key"></i> ${I18n.change_skin}</a>
                            </li>
                            <li id="logoutBtn" >
                                <a href="javascript:" style="height: 30px;padding: 3px 25px;" ><i class="fa fa-sign-out"></i> ${I18n.logout_btn}</a>
                            </li>
                        </ul>

                    </li>
                </ul>
            </div>

        </nav>
    </header>
    <!-- 2-header end -->

    <!-- 3-left start -->
    <aside class="main-sidebar">
        <section class="sidebar" style="height: auto;" >
            <!-- sidebar menu -->
            <ul class="sidebar-menu" data-widget="tree" >
                <li class="header">${I18n.system_nav}</li>
                <#if resourceList?? && resourceList?size gt 0>
                    <@renderMenu resourceList />
                </#if>
            </ul>
            <#-- render menu -->
            <#macro renderMenu resourceList >
                <#list resourceList as resource>
                    <#if resource.type ==0>
                        <#-- catalog -->
                        <li class="treeview" style="height: auto;"  >
                            <a href="javascript:void(0);">
                                <i class="fa ${resource.icon}"></i>
                                <span>${resource.name}</span>
                                <span class="pull-right-container">
                                    <i class="fa fa-angle-left pull-right"></i>
                                </span>
                            </a>
                            <ul class="treeview-menu" >
                                <#if resource.children?? && resource.children?size gt 0>
                                    <@renderMenu resource.children />
                                </#if>
                            </ul>
                        </li>
                    <#elseif resource.type ==1>
                        <#-- mainMenu -->
                        <#if !(mainMenu?exists) >
                            <#assign mainMenu = resource />
                        <#elseif resource.order lt mainMenu.order >
                            <#assign mainMenu = resource />
                        </#if>
                        <#-- menu -->
                        <li class="nav-click">
                            <#-- url -->
                            <#assign resourceUrl = request.contextPath + resource.url />
                            <#if resource.url?starts_with("http") >
                                <#assign resourceUrl = resource.url />
                            </#if>
                            <#-- menu -->
                            <a class="J_menuItem" href="${resourceUrl}">
                                <i class="fa ${resource.icon}"></i>
                                <span>${resource.name}</span>
                            </a>
                        </li>
                    </#if>
                </#list>
            </#macro>
        </section>
    </aside>
    <!-- 3-left end -->

    <!-- 4-right start -->
    <div class="content-wrapper">

        <!-- Tabs -->
        <div class="content-tabs">
            <!-- left -->
            <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i></button>

            <!-- Tab -->
            <nav class="page-tabs J_menuTabs">
                <div class="page-tabs-content">
                    <#-- mainPage -->
                    <#if mainMenu?exists >
                        <a href="javascript:;" class="active J_menuTab noactive" data-id="${request.contextPath}${mainMenu.url}">${mainMenu.name}</a>
                    </#if>
                </div>
            </nav>

            <!-- right -->
            <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i></button>
            <!-- opt -->
            <div class="btn-group roll-nav roll-right">
                <button class="dropdown" data-toggle="dropdown">${I18n.tab_opt}<span class="caret"></span></button>
                <ul role="menu" class="dropdown-menu dropdown-menu-right">
                    <li class="tabCloseCurrent"><a>${I18n.tab_close_current}</a></li>
                    <li class="J_tabCloseOther"><a>${I18n.tab_close_other}</a></li>
                    <li class="J_tabCloseAll"><a>${I18n.tab_close_all}</a></li>
                </ul>
            </div>
            <!-- refresh -->
            <a href="#" class="roll-nav roll-right tabReload"><i class="fa fa-refresh"></i> ${I18n.tab_refresh}</a>
            <!-- fullscreen -->
            <a href="#" class="roll-nav roll-right fullscreen" id="fullScreen"><i class="fa fa-arrows-alt"></i></a>
        </div>
        <!-- Iframe Content -->
        <div class="J_mainContent" id="content-main" >
            <!-- Iframe -->
            <iframe class="J_iframe" width="100%" height="100%" src="${request.contextPath}${mainMenu.url}" frameborder="0" data-id="${request.contextPath}${mainMenu.url}" seamless></iframe>
        </div>

    </div>
    <!-- 4-right end -->

    <!-- 5-footer start -->
    <footer class="main-footer">
        Powered by <b>XXL-MQ</b> ${I18n.admin_version}
        <div class="pull-right hidden-xs">
            <strong>Copyright &copy; 2015-${.now?string('yyyy')} &nbsp;
                <a href="https://www.xuxueli.com/" target="_blank" >xuxueli</a>
                &nbsp;
                <a href="https://github.com/xuxueli/xxl-mq" target="_blank" >github</a>
            </strong><!-- All rights reserved. -->
        </div>
    </footer>
    <!-- 5-footer end -->

</div>

<!-- 6-script start -->
<@netCommon.commonScript />
<script src="${request.contextPath}/static/biz/common/admin.tab.js"></script>
<script src="${request.contextPath}/static/biz/common/admin.setting.js"></script>
<script>
    $(function () {
        // init admin tab
        $.adminTab.initTab();
    });
</script>
<!-- 6-script end -->

</body>
</html>
