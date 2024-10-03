import { AxiosResponse } from "axios"
import { IUserWrapper } from "../interface-contract/user/user-wrapper"
import { JwtModel } from "../interface-contract/jwt/jwt-model"
import { IUser } from "../interface-contract/user/user-model"
import { HttpMainService } from "./http-main.service"
import { Csrf } from "../interface-contract/csrf/csrf.type"
import { GroupUserModel } from "../interface-contract/group/group-user-model"
import { GroupModel } from "../interface-contract/group/group-model"
import { FullTextSearchResponseType } from "../interface-contract/search-text/FullTextSearchResponseType"

export class HttpGroupService extends HttpMainService {

  public constructor() {
    super()
  }

  public getCsrfToken(): Promise<AxiosResponse<Csrf>> {
    return this.instance.get("csrf")
  }

  public authenticate(jwtModel: JwtModel): Promise<AxiosResponse<IUser>> {
    return this.instance.post("auth", jwtModel)
  }

  public async pingRoute(): Promise<AxiosResponse<IUserWrapper>> {
    return this.instance.get("fetch")
  }

  public logout(): Promise<AxiosResponse> {
    return this.instance.get("logout")
  }

  createUser(firstname: string, lastname: string, email: string, password: string): Promise<AxiosResponse> {
    return this.instance.post("users/register", {
      firstname,
      lastname,
      email,
      password
    })
  }

  public fetchAllUsersInConversation(groupUrl: string): Promise<AxiosResponse<GroupUserModel[]>> {
    return this.instance.get(`users/group/${groupUrl}`)
  }

  public fetchAllUsersWithoutAlreadyInGroup(groupUrl: string): Promise<AxiosResponse<GroupUserModel[]>> {
    return this.instance.get("users/all/" + groupUrl, {})
  }

  public addUserToGroup(userId: number | string, groupUrl: string): Promise<AxiosResponse> {
    return this.instance.get("users/add/" + userId + "/" + groupUrl)
  }

  public getMultimediaFiles(groupUrl: string) {
    return this.instance.get<string[]>(`files/groupUrl/${groupUrl}`)
  }

  public createGroup(groupName: string): Promise<AxiosResponse<GroupModel>> {
    return this.instance.post("create", { name: groupName })
  }
  public searchInConversations(data: { text: string }) {
    return this.instance.post<FullTextSearchResponseType>("search", data)
  }
  public uploadFile(data: FormData): Promise<AxiosResponse> {
    return this.instance.post("upload", data)
  }
  public markMessageAsSeen(userId: number, groupUrl: string) {
    return this.instance.get<Date>(`messages/seen/group/${groupUrl}/user/${userId}`)
}
}
