<#include "security.ftl">
<#import "pager.ftl" as p>
<@p.pager url!0 page!0 />
<div class="card-columns" id="message-list">
    <#list page.content as message>
        <div class="card my-3" data-id="${message.id}">
            <div class="card-img-top">
                <#if message.filename??>
                    <img src="/img/${message.filename}"/>
                </#if>
            </div>
            <div class="m-2">
                <span>${message.text}</span><br/>
                <i>#${message.tag}</i>
            </div>
            <div class="card-footer text-muted">
                <a href="/user-messages/${message.author.id}" >${message.authorName}</a>
                <#if message.author.id == currentUserId>
                    <a class = "btn btn-primary" href="/user-messages/${message.author.id}?message=${message.id}" >Edit</a>
                </#if>
            </div>
        </div>

    <#else >
        No messages
    </#list>
</div>
<@p.pager url page/>