Vue.component("add-image-post", {
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
            if (this.imageUrl === null) {
                alert("Please upload image.");
                return;
            }
            let newImagePost = {
                type: "IMAGE_POST",
                imageName: this.imageName,
                base64Image: this.imageUrl.split(',')[1],
            };

            if (this.description !== null && this.description.trim() !=='') {
                newImagePost.text = this.description;
            }

            window.API.post("posts", newImagePost).then(res => {
                alert("Successfully added new image");
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
                <a class="nav-link active" id="images-tab" data-toggle="tab" role="tab" aria-controls="images" aria-selected="true" href="#images">Image</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="posts-tab" data-toggle="tab" href="#posts" role="tab" aria-controls="posts" aria-selected="false">Optional description</a>
            </li>
        </ul>
    </div>
    <div class="card-body">
        <div class="tab-content" id="myTabContent">
            <div class="tab-pane fade show active" id="images" role="tabpanel" aria-labelledby="images-tab">
                <div class="form-group">
                    <img v-if="imageUrl !== null" v-bind:src="imageUrl" class="addPostImageIconBig">
                    <br/>
                    <hr>
                    <input type="file" ref="postImage" @change="previewImage" class="registration-btnSubmit" value="Upload" />
                    <br/>
                    <hr/>
                    <input v-on:click="reset($event)" type="button" class="registration-btnSubmit" value="Reset" />
                </div>
                <div class="py-4"></div>
            </div>
            <div class="tab-pane fade" id="posts" role="tabpanel" aria-labelledby="posts-tab">
                <div class="form-group">
                    <label class="sr-only" for="message">post</label>
                    <textarea v-model="description" class="form-control" id="message" rows="3" placeholder="Add description..."></textarea>
                </div>

            </div>
        </div>
        <div class="btn-toolbar justify-content-between">
            <div class="btn-group">
                <button v-on:click="postClicked()" type="submit" class="btn btn-primary">upload image</button>
            </div>
        </div>
    </div>
</div>
    `


});