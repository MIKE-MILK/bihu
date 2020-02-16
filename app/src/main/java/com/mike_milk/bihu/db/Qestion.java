package com.mike_milk.bihu.db;

public class Qestion {
    private String status;
    private String info;
    private Data data;
    public static class Data{
        private String totalCount;
        private String totalPage;
        private Qestions qestions;
        public static class Qestions{
            private String id;
            private String title;
            private String content;
            private String images;
            private String date;
            private String exciting;
            private String naive;
            private String recent;
            private String answerCount;
            private String authorId;
            private String authorName;
            private String authorAvatar;
            private String is_exciting;
            private String is_naive;
            private String is_favorite;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAnswerCount() {
                return answerCount;
            }

            public void setAnswerCount(String answerCount) {
                this.answerCount = answerCount;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getAuthorAvatar() {
                return authorAvatar;
            }

            public void setAuthorAvatar(String authorAvatar) {
                this.authorAvatar = authorAvatar;
            }

            public String getAuthorId() {
                return authorId;
            }

            public void setAuthorId(String authorId) {
                this.authorId = authorId;
            }

            public String getAuthorName() {
                return authorName;
            }

            public void setAuthorName(String authorName) {
                this.authorName = authorName;
            }

            public String getExciting() {
                return exciting;
            }

            public void setExciting(String exciting) {
                this.exciting = exciting;
            }

            public String getImages() {
                return images;
            }

            public void setImages(String images) {
                this.images = images;
            }

            public String getIs_exciting() {
                return is_exciting;
            }

            public String getIs_favorite() {
                return is_favorite;
            }

            public String getNaive() {
                return naive;
            }

            public String getIs_naive() {
                return is_naive;
            }

            public String getRecent() {
                return recent;
            }

            public void setIs_exciting(String is_exciting) {
                this.is_exciting = is_exciting;
            }

            public void setIs_favorite(String is_favorite) {
                this.is_favorite = is_favorite;
            }

            public void setIs_naive(String is_naive) {
                this.is_naive = is_naive;
            }

            public void setNaive(String naive) {
                this.naive = naive;
            }

            public void setRecent(String recent) {
                this.recent = recent;
            }
        }

        public Qestions getQestions() {
            return qestions;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setQestions(Qestions qestions) {
            this.qestions = qestions;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }
    }

    public Data getData() {
        return data;
    }

    public String getInfo() {
        return info;
    }

    public String getStatus() {
        return status;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
