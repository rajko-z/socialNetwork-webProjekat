Vue.component("find-user-page", {
    data: function () {
        return {
            name: null,
            surname: null,
            username: null
        }
    },

    mounted() {

    },

    methods: {
    },


    template: `
<div>
    <h2>Find user page</h2>
    <v-row> 
        <input v-model="name" type="text" class="form-control" placeholder="Your Username *" value="" />
        <input v-model="surname" type="text" class="form-control" placeholder="Your Username *" value="" />
        <input v-model="username" type="text" class="form-control" placeholder="Your Username *" value="" />
    </v-row>
</div>
    `


});