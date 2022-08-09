<template>
<v-card flat>
    <v-card-text>
        <v-form ref="form">
            <h1 class="my-6">First merge file:</h1>
            <v-file-input name="mergeFileInput" :rules="ruleFiles" label="upload local file: " truncate-length="15" v-model="fileupload" data-cy="file1"></v-file-input>

            <h1 class="my-6">Second merge file:</h1>
            <v-text-field :rules="ruleInput" label="Id" outlined v-model="file2" data-cy="file2"></v-text-field>
        <v-text-field :rules="ruleInputText" v-model="name" label="Sub project name" outlined class="shrink mx-11" data-cy="name22"></v-text-field>
        <v-textarea :rules="ruleInputText" v-model="description" label="Description" auto-grow counter maxlength="350" class="shrink mx-11" outlined rows="1" data-cy="desc22"></v-textarea>
            <v-btn text name="mergeButton" class="blue white--text mx-0 mt-3" @click="uploadFile" :loading="loading[0]">Merge Projects</v-btn>
        </v-form>
    </v-card-text>
    <SnackBar :response="response"></SnackBar>
</v-card>
</template>

<script>
import SnackBar from './buttons/SnackBar.vue';
import Vue from 'vue'
export default {
    components: {
        SnackBar
    },
    name: "FunctionHub",
    data() {
        return {
            ruleFiles: [
                value => value.size >= 0 || 'need a file!'
            ],
            ruleInput: [
                value => value >= 3 || 'required!'
            ],
            ruleInputText: [
                (value) => !!value || "required"
            ],
            fileupload: [],
            name: null,
            description: null,
            response: "",
            file2: null,
            loading: [false]
        }
    },
    methods: {
        async uploadFile() {
            if (this.$refs.form.validate()) {
                Vue.set(this.loading, 0, true)
                let formData = new FormData();
                formData.append("file", this.fileupload)
                this.response = await fetch(process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/merge?mergeFile2=" + this.file2 +
                    "&name=" + this.name +
                    "&description=" + this.description,

                    {
                        method: "POST",
                        body: formData
                    })
            }
            Vue.set(this.loading, 0, false)
        }

    }
}
</script>
