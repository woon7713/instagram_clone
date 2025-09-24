import api from "./api";

const likeService = {
  toggleLike: async (postId) => {
    set({ loading: true, error: null });

    try {
      const response = await postService.toggleLike(postId);

      console.log(response);

      const { isLiked, likeCount } = response.data;

      set((state) => ({
        likes: {
          ...state.likes,
          [postId]: {
            isLiked,
            likeCount,
          },
        },
        loading: false,
      }));

      return { isLiked, likeCount };
    } catch (err) {
      set({
        loading: false,
        error: err.response?.data?.message || "Failed to toggle like",
      });
      throw err;
    }
  },

  countLike: async (postId) => {
    const response = await api.get(`/api/likes/count/${postId}`);
    return response.data;
  },

  isLiked: async (postId) => {
    const response = await api.get(`/api/likes/is-liked/${postId}`);
    return response.data;
  },
};

export default likeService;
