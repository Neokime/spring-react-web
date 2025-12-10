import api from "./base.service";

class UploadService {
  uploadFile(formData) {
  return api.post("/files/upload", formData, {
    headers: { "Content-Type": "multipart/form-data" }
  });
}

}

export default new UploadService();
