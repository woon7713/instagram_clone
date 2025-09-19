import api from "./api";

const postService = {
  getAllPosts: async (page = 0, size = 10) => {
    const response = await api.get("/api/posts", {
      params: { page, size },
    });
    return response.data;
  },
};

export default postService;
