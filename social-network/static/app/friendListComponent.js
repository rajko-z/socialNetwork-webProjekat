Vue.component("friend-list", {
    props: ['username'],
    data: function () {
        return {
            currentUser: window.getCurrentUser(),
            friends: [],
        }
    },

    mounted: function() {
        if (window.getCurrentUser() === null) {
            router.push('/');
        }
        if (window.getCurrentUser().username === this.username) {
            window.API.get("friendStatus/getFriends").then(res => {
                this.friends = res.data;
            }).catch(err => {
                alert(err.response.data);
            })
        } else {
            window.API.get("friendStatus/getCommonFriends/" + this.username).then(res => {
                this.friends = res.data;
            }).catch(err => {
                alert(err.response.data);
            })
        }
    }
    ,

    template: `
<div class="friendListContainer">
    <h5 v-if="currentUser.username===username" style="color:white">My friends ({{friends.length}})</h5>
    <h5 v-else style="color:white">Common friends ({{friends.length}})</h5>
    <hr>
    <user-info v-for="friend in friends" :user="friend" view="small"></user-info>
</div>
    `


});