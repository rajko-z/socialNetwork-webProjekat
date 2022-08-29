package services;

import dto.NewPostDTO;
import exceptions.BadRequestException;
import exceptions.InternalAppException;
import model.Image;
import model.Post;
import model.PostType;
import model.User;
import repository.PostRepository;
import repository.RepoFactory;
import util.Constants;
import validation.ValidationService;

import java.time.LocalDateTime;

public class PostService {
    private final PostRepository postRepository;
    private final ImageService imageService;
    private final ValidationService validationService;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
        this.validationService = new ValidationService();
        this.imageService = new ImageService(RepoFactory.imageRepo);
    }

    public Post addPost(NewPostDTO newPostDTO, User user) {
        String errorMsg = validationService.validateAndGetErrorMessage(newPostDTO);
        if (errorMsg != null) {
            throw new BadRequestException(errorMsg);
        }
        switch (newPostDTO.getType()) {
            case REGULAR_POST: {
                return createRegularPost(newPostDTO, user);
            }
            case IMAGE_POST:
            case PROFILE_POST:{
                return createImagePost(newPostDTO, user);
            }
            default:
                throw new BadRequestException("Unknown post type: " + newPostDTO.getType().toString());
        }
    }

    private Post createRegularPost(NewPostDTO newPostDTO, User user) {
        if (newPostDTO.getText() == null || newPostDTO.getText().trim().isEmpty())
            throw new BadRequestException("Regular post can't have empty content");

        Image postImage = null;
        if (newPostDTO.getBase64Image() != null) {
            if (newPostDTO.getImageName() == null || newPostDTO.getImageName().trim().isEmpty()){
                throw new BadRequestException("Image name should be specified");
            }
            postImage = imageService.createImageFromBase64String(newPostDTO.getBase64Image(), newPostDTO.getImageName());
            if (postImage == null) {
                throw new InternalAppException("Error happened while adding image for post");
            }
        }

        Post post = new Post(newPostDTO.getText(), newPostDTO.getType(), LocalDateTime.now(), false);
        post.setImage(postImage);
        Post addedPost = this.postRepository.add(post);
        user.addNewPost(post);
        saveDataAfterAddingPost();
        return addedPost;
    }

    private void saveDataAfterAddingPost() {
        this.postRepository.saveData(Constants.FILE_POSTS_HEADER);
        RepoFactory.userRepo.saveData(Constants.FILE_USERS_HEADER);
        RepoFactory.imageRepo.saveData(Constants.FILE_IMAGES_HEADER);
    }

    private Post createImagePost(NewPostDTO newPostDTO, User user) {
        if (newPostDTO.getBase64Image() == null || newPostDTO.getBase64Image().trim().isEmpty()) {
            throw new BadRequestException("Base64 field must not be null or empty");
        }
        if (newPostDTO.getImageName() == null || newPostDTO.getImageName().trim().isEmpty()) {
            throw new BadRequestException("Image name must be specified");
        }
        Image postImage = imageService.createImageFromBase64String(newPostDTO.getBase64Image(), newPostDTO.getImageName());
        if (postImage == null) {
            throw new InternalAppException("Error happened while adding image for post");
        }
        Post post = new Post(newPostDTO.getText(), newPostDTO.getType(), LocalDateTime.now(), false);
        post.setImage(postImage);
        Post addedPost = this.postRepository.add(post);
        user.addNewPost(post);

        if (newPostDTO.getType().equals(PostType.PROFILE_POST))
            user.setProfileImage(addedPost);
        saveDataAfterAddingPost();
        return addedPost;
    }

    public String getImageUrlForPost(Post post) {
        if (post.containsImage()) {
            String imageName = post.getImage().getName();
            return Constants.IMAGE_URL_PATH + imageName;
        }
        return null;
    }
}
