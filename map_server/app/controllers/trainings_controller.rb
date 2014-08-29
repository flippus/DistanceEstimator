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
      if params[:training] != nil && params[:training][:location] != nil && params[:training][:datetime] != nil
        locations = params[:training][:location]

        if locations.count > 0
          training = Training.create!(:created_at => Time.at((params[:training][:datetime].to_i/1000)))

          locations.each do |xml_location|
            if xml_location["latitude"] != nil && xml_location["longitude"] != nil
              location = Location.new
              location.latitude = xml_location["latitude"]
              location.longitude = xml_location["longitude"]
              location.training_id = training.id
              location.save!
            end
          end
        end
      end
    rescue TimeoutError => e
      MyLog.debug "timeout #{e}"
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

end
