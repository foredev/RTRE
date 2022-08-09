<template>
<v-card class="pb-4">
    <v-form ref="form">
        <v-card-text>
            <v-file-input :rules="ruleFiles" label="Upload local IFC file: " truncate-length="15" v-model="fileupload" name="fileButtonCheckIn" data-cy="fileCheckin"></v-file-input>
        </v-card-text>
        <v-text-field :rules="ruleInput" v-model="parentOid" label="Head Project Id" outlined class="shrink mx-11" data-cy="HeadId"></v-text-field>
        <v-text-field :rules="ruleInput" v-model="name" label="Sub project name" outlined class="shrink mx-11" data-cy="name"></v-text-field>
        <v-textarea :rules="ruleInput" v-model="description" label="Description" auto-grow counter maxlength="350" class="shrink mx-11" outlined rows="1" data-cy="desc"></v-textarea>
        <v-btn class="ml-11" color="blue white--text" @click="checkIn()" :loading="loading[0]">
            Submit
        </v-btn>
    </v-form>
    <SnackBar :response="response"></SnackBar>
</v-card>
</template>

<script>
import SnackBar from "./buttons/SnackBar.vue";
import Vue from "vue";
export default {
    name: "CheckIn",
    data() {
        return {
            ruleFiles: [(value) => value.size >= 0 || "need a file!"],
            ruleInput: [(value) => !!value || "required"],
            fileForCheckin: null,
            name: null,
            description: null,
            parentOid: null,
            fileupload: [],
            response: "",
            loading: [false],
        };
    },
    methods: {
        async checkIn() {
            if (this.$refs.form.validate()) {
                Vue.set(this.loading, 0, true);
                let formData = new FormData();
                formData.append("file", this.fileupload);
                this.response = await fetch(
                    process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/postIfcAsSubProject?" +
                    "parentPoid=" +
                    this.parentOid +
                    "&name=" +
                    this.name +
                    "&description=" +
                    this.description, {
                        method: "POST",
                        body: formData,
                    }
                );
            }
            Vue.set(this.loading, 0, false);
        },
    },
    components: {
        SnackBar,
    },
};
</script>
