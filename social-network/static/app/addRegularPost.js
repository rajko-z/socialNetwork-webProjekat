Vue.component("add-regular-post", {
    data: function () {
        return {
            description: null,
            loggedUser: window.getCurrentUser(),
            imageUrl: null,
            imageName: null

        }
    },

    computed: {

    },
    methods: {

        getBase64: function (file) {
            return new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => resolve(reader.result);
                reader.onerror = error => reject(error);
            });
        },

        previewImage: function (event) {
            let files = event.target.files;
            console.log(files);
            if (files.length > 0) {
                let file = files[0];
                console.log("ispisujem fajl");
                console.log(file);


                if (file['type'].split('/')[0] !== 'image') {
                    alert("Please choose image");
                    this.reset();
                    return;
                }

                this.getBase64(file).then(
                    data => {
                        this.imageUrl = data;
                        this.imageName = file.name;
                        console.log(data);
                    }
                );
            }
        },

        reset: function () {
            this.imageUrl = null;
            this.imageName = null;
            this.$refs.postImage.value = null;
        },

        userProfilePage: function () {
            let username = window.getCurrentUser().username;
            router.push({
                name: 'userProfilePage',
                params: {
                    username: username
                }
            });
            this.$router.go();
        },


        postClicked: function() {
            if (this.description === null || this.description.trim() === '') {
                alert("Please fill description.");
                return;
            }
            let newRegularPost = {
                text: this.description,
                type: "REGULAR_POST",
                imageName: this.imageName,
            };

            if (this.imageUrl !== null) {
                newRegularPost.base64Image = this.imageUrl.split(',')[1];
            }

            window.API.post("posts", newRegularPost).then(res => {
                alert("Successfully added new post");
                this.userProfilePage();
            }).catch(err => {
                alert("Error:" + err.response.data);
            });

        }

    },


    template: `
<div class="card">
    <div class="card-header">
        <ul class="nav nav-tabs card-header-tabs" id="myTab" role="tablist">
            <li class="nav-item">
                <a class="nav-link active" id="posts-tab" data-toggle="tab" href="#posts" role="tab" aria-controls="posts" aria-selected="true">Description</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="images-tab" data-toggle="tab" role="tab" aria-controls="images" aria-selected="false" href="#images">Optional image</a>
            </li>
        </ul>
    </div>
    <div class="card-body">
        <div class="tab-content" id="myTabContent">
            <div class="tab-pane fade show active" id="posts" role="tabpanel" aria-labelledby="posts-tab">
                <div class="form-group">
                    <label class="sr-only" for="message">post</label>
                    <textarea v-model="description" class="form-control" id="message" rows="3" placeholder="Share your thoughts with the world..."></textarea>
                </div>

            </div>
            <div class="tab-pane fade" id="images" role="tabpanel" aria-labelledby="images-tab">
                <div class="form-group">
                    <img v-if="imageUrl !== null" v-bind:src="imageUrl" class="addPostImageIconBig">
                    <br/>
                    <hr>
                    <input type="file" ref="postImage" @change="previewImage" class="registration-btnSubmit" value="Upload" />
                    <br/>
                    <hr/>
                    <input v-on:click="reset($event)" type="button" class="btn btn-outline-primary" value="Reset" />
                </div>
                <div class="py-4"></div>
            </div>
        </div>
        <div class="btn-toolbar justify-content-between">
            <div class="btn-group">
                <button v-on:click="postClicked()" type="submit" class="btn btn-primary">post</button>
            </div>
        </div>
    </div>
</div>
    `


});