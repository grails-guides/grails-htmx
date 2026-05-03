<div id="taskForm-errors" class="text-danger small mt-2">
    <g:eachError bean="${task}"><div>${message(error: it).encodeAsHTML()}</div></g:eachError>
</div>
