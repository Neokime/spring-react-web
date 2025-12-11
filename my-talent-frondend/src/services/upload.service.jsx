import api from "./base.service";

class UploadService {
  uploadFile(formData) {
    return api.post("/files/upload", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
  }

  uploadSingle(file) {
    const formData = new FormData();
    formData.append("file", file);
    return this.uploadFile(formData);
  }
}

export default new UploadService();
