import os
import json
import boto3

# exception handling: https://boto3.amazonaws.com/v1/documentation/api/latest/guide/error-handling.html
from boto3.exceptions import S3UploadFailedError
from botocore.exceptions import ClientError


class S3Manager:
    # boto3 docs
    # https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/s3.html

    SECRET_KEY_JSON = os.path.join(
        os.path.dirname(os.path.abspath(__file__)), "../secret-key.json"
    )
    REGION_NAME = "ap-northeast-2"
    BUCKET_NAME = "make-you-shorts"
    BUCKET_DIRECTORY = "shorts"
    EXPIRATION_TIME = 3600  # 1 hour
    END_POINT_URL = "https://s3.ap-northeast-2.amazonaws.com"

    def __init__(self):
        self._credentials = json.load(open(self.SECRET_KEY_JSON))
        self.s3_client = boto3.client(
            "s3",
            aws_access_key_id=self._credentials["AWS_ACCESS_KEY"],
            aws_secret_access_key=self._credentials["AWS_ACCESS_SECRET_KEY"],
            region_name=self.REGION_NAME,
            config=(boto3.session.Config(signature_version="s3v4")),
            endpoint_url=self.END_POINT_URL,
        )

    def get_video_pre_signed_url(self, file_name):
        """S3 에 저장된 비디오 pre-signed URL 조회

        :param file_name: 비디오 파일명 w.format
        :return: S3 pre-signed URL
        """
        s3_pre_signed_url = self.s3_client.generate_presigned_url(
            "get_object",
            Params={
                "Bucket": self.BUCKET_NAME,
                "Key": f"{self.BUCKET_DIRECTORY}/{file_name}",
            },
            ExpiresIn=self.EXPIRATION_TIME,
        )
        return s3_pre_signed_url

    def upload_video(self, video_file_path, file_name):
        """

        :param video_file_path: 비디오 파일 full path (temp 폴더)
        :param file_name: 비디오 파일명 w.format
        :return:
        """
        try:
            self.s3_client.upload_file(
                video_file_path,
                self.BUCKET_NAME,
                f"{self.BUCKET_DIRECTORY}/{file_name}",
            )
        except S3UploadFailedError as e:
            raise e
        except ClientError as e:
            raise e
