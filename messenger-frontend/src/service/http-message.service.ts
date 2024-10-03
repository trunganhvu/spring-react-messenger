import { WrapperMessageModel } from "../interface-contract/message/wrapper-message-model"
import {HttpMainService} from "./http-main.service"
import {AxiosResponse} from "axios"

export class HttpMessageService extends HttpMainService {
    public constructor() {
        super()
    }

    public getMessages(groupUrl: string, offset: number): Promise<AxiosResponse<WrapperMessageModel>> {
        return this.instance.get<WrapperMessageModel>(`/messages/${offset}/group/${groupUrl}`)
    }
}
