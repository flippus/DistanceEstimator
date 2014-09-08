class TrainingsController < ApplicationController
  skip_before_action :verify_authenticity_token, :export

  # GET /trainings
  # GET /trainings.json
  def index
    @trainings = Training.all

    if params[:id]
      @training = Training.find_by_id((params[:id]))
    else
      @training = Training.last
    end

    respond_to do |format|
      format.html
    end
  end

  def export
    begin
      if !training_params.nil? && !training_params[:location].nil? && !training_params[:datetime].nil?
        locations = training_params[:location]

        if locations.count > 0
          training = Training.create! created_at: Time.at((training_params[:datetime].to_i/1000))

          locations.each do |xml_location|
            if !xml_location["latitude"].nil? && !xml_location["longitude"].nil?
              Location.create! latitude: xml_location["latitude"], longitude: xml_location["longitude"], training_id: training.id
            end
          end
        end
      end
    rescue TimeoutError => e
      logger.info "timeout #{e}"
    end

    render :nothing => true
  end

  # DELETE /trainings/1
  # DELETE /trainings/1.json
  def destroy
    @training = Training.find(params[:id])
    @training.destroy

    respond_to do |format|
      format.html { redirect_to trainings_url }
    end
  end

  private

  def training_params
      params.require(:training).permit(:datetime, location: [:latitude, :longitude, :training_id])
  end

end
