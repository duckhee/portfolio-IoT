const ckConfig = {
    language: 'ko',
    height: 450,
    removePlugins: 'resize',
    resize_enabled: false,
    toolbarGroups: [
        {name: 'document', groups: ['mode', 'document', 'doctools']},
        {name: 'clipboard', groups: ['clipboard', 'undo']},
        {name: 'editing', groups: ['find', 'selection', 'spellchecker', 'editing']},
        {name: 'forms', groups: ['forms']},

        {name: 'basicstyles', groups: ['basicstyles', 'cleanup']},
        {name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi', 'paragraph']},
        {name: 'links', groups: ['links']},
        {name: 'insert', groups: ['insert']},
        {name: 'styles', groups: ['styles']},
        {name: 'colors', groups: ['colors']}
    ],
    format_tags: 'p;h1;h2;h3;pre',
    enterMode: CKEDITOR.ENTER_BR,
    removeDialogTabs: 'image:advanced;link:advanced',
    htmlEncodeOutput: false,
    // filebrowserUploadMethod: "form", // file upload javascript function
    // fileTools_requestHeaders:{}
    filebrowserUploadUrl: "/blogs/resources/upload?[[${_csrf.parameterName}]]=[[${_csrf.token}]]"
};

CKEDITOR.on('dialogDefinition', function (ev) {
    var dialogName = ev.data.name;
    var dialogDefinition = ev.data.definition;

    switch (dialogName) {
        case 'image': //Image Properties dialog
            console.log("information ::: ", dialogDefinition);
            dialogDefinition.removeContents('Link');

            dialogDefinition.removeContents('info');
            // dialogDefinition.removeContents('advanced');

            break;
    }
});