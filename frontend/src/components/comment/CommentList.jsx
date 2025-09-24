import { useEffect } from "react";
import useCommentStore from "../../store/commentStore";
import CommentItem from "./CommentItem";

const CommentList = ({ postId }) => {
  const { fetchComments, comments } = useCommentStore();

  useEffect(() => {
    const getComments = async () => {
      try {
        await fetchComments(postId);
      } catch (err) {
        console.error(err);
      }
    };

    if (!postId) return;

    getComments();
  }, [postId]);

  return (
    <div>
      {comments.map((comment) => (
        <CommentItem key={comment.id} comment={comment} />
      ))}
    </div>
  );
};

export default CommentList;
