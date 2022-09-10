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
        if(window.getCurrentUser().role === "ADMIN" && window.getCurrentUser().username === this.username)
        {
            window.API.get("allRegular").then(res => {
                this.friends = res.data;
            }).catch(err => {
                alert(err.response.data);
            })

        }
        else{

        if (window.getCurrentUser().username === this.username) {
            window.API.get("friendStatus/getFriends").then(res => {
                this.friends = res.data;
            }).catch(err => {
                alert(err.response.data);
            })
        } else {
            if(window.getCurrentUser().role === "REGULAR")
            {
            window.API.get("friendStatus/getCommonFriends/" + this.username).then(res => {
                this.friends = res.data;
            }).catch(err => {
                alert(err.response.data);
            })}
            else{
                window.API.get("/getFriendsOfUser/" + this.username).then(res => {
                    this.friends = res.data;
                }).catch(err => {
                    alert(err.response.data);
                })}

            }
        }}
        // window.API.get("users/all").then(res => {
        //     this.friends = res.data;
        // }).catch(err => {
        //     alert(err.response.data);
        // })

    ,

    template: `
<div class="friendListContainer">
    <h5 v-if="currentUser.username===username" style="color:darkblue">My friends ({{friends.length}})</h5>
    <h5 v-else-if="currentUser.role === 'ADMIN'"style="color:darkblue">Friends of {{username}} ({{friends.length}})</h5>
    <h5 v-else style="color:darkblue">Common friends ({{friends.length}})</h5>
    <hr>
    <user-info v-for="friend in friends" :user="friend" view="small"></user-info>
</div>
    `


});